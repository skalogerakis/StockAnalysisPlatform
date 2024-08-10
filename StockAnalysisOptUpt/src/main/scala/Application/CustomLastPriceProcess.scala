/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package Application

import Schemas.{BatchInternalSchema, EventUnpackSchema, LastPriceOutputSchema, WindowInternalSchema}
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector

import scala.annotation.tailrec
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`


class CustomLastPriceProcess extends KeyedProcessFunction[String, EventUnpackSchema, LastPriceOutputSchema] {

	@transient private var _max_batch_id: ValueState[Long] = _
	@transient private var _completed_batch: ValueState[Long] = _
	@transient private var _reported_ts: ValueState[Long] = _
	@transient private var _window_state: MapState[Long, WindowInternalSchema] = _
	@transient private var _batch_state: MapState[Long, BatchInternalSchema] = _

	override def open(parameters: Configuration): Unit = {
		_window_state = getRuntimeContext.getMapState(new MapStateDescriptor[Long, WindowInternalSchema]("windowState", classOf[Long], classOf[WindowInternalSchema]))
		_batch_state = getRuntimeContext.getMapState(new MapStateDescriptor[Long, BatchInternalSchema]("batchState", classOf[Long], classOf[BatchInternalSchema]))
		_max_batch_id = getRuntimeContext.getState(new ValueStateDescriptor("maxBatchID_win", classOf[Long]))
		_completed_batch = getRuntimeContext.getState(new ValueStateDescriptor("completedBatchID", classOf[Long], -1))
		_reported_ts = getRuntimeContext.getState(new ValueStateDescriptor("_reported_ts", classOf[Long], -1))
	}


	override def processElement(i: EventUnpackSchema,
								ctx: KeyedProcessFunction[String, EventUnpackSchema, LastPriceOutputSchema]#Context,
								out: Collector[LastPriceOutputSchema]): Unit = {

		i.price match {
			case -2.0 =>
				//Special tuple for last_in_sequence
				_max_batch_id.update(i.batch_ID)
				MarkExpectingBatches(wait_batches_id = i.wait_batches.get)
				CloseWindows(latest_completed = SafeToReportBatchId(i.symbol, i.batch_ID, out), i = i, out = out)
			case -1.0 =>

				//Update the state of the batch as one which is only lookup
				_batch_state.put(i.batch_ID, BatchInternalSchema(closed_status = 2,
																 is_lookup = true,
																 lookup_size = i.lookup_size,
																 latest_ts = i.timestamp,
																 sec_type = i.sec_type))

				CloseWindows(latest_completed = SafeToReportBatchId(symbol = i.symbol, completed_batch_id = _max_batch_id.value(), output = out), i = i, out = out)
			case _ =>
				//All the remaining symbols
				val current_window_group: Long = WindowGrouping(current_ts = i.timestamp)
				val cur_window_state: Option[WindowInternalSchema] = Option(_window_state.get(current_window_group))

				(i.last_symbol_in_batch, cur_window_state) match {

					case (false, Some(value)) =>

						/**
						 * Calculation of last price per window
						 */

						if (value.last_timestamp < i.timestamp) {
							_window_state.put(current_window_group, value.copy(last_timestamp = i.timestamp, last_price = i.price))
						} else if (value.last_timestamp == i.timestamp && value.last_price < i.price) {
							_window_state.put(current_window_group, value.copy(last_timestamp = i.timestamp, last_price = i.price))
						}
					case (false, None) =>
						/*
							Initialization if there is not the desired window. Aligns everything to the correct place and assigns the starting window time
						*/
						_window_state.put(current_window_group, WindowInternalSchema(window_group = current_window_group,
																					 last_timestamp = i.timestamp,
																					 last_price = i.price,
																					 affected_batches = Map(i.batch_ID -> false)))
					case (true, Some(value)) =>

						/**
						 * Calculation of last price per window
						 */


						if (value.last_timestamp < i.timestamp) {
							_window_state.put(current_window_group, value.copy(last_timestamp = i.timestamp,
																			   last_price = i.price,
																			   affected_batches = value.affected_batches + (i.batch_ID -> true)))

						} else if (value.last_timestamp == i.timestamp && value.last_price < i.price) {
							_window_state.put(current_window_group, value.copy(last_timestamp = i.timestamp,
																			   last_price = i.price,
																			   affected_batches = value.affected_batches + (i.batch_ID -> true)))
						} else {
							_window_state
								.put(current_window_group, value.copy(affected_batches = value.affected_batches + (i.batch_ID -> true)))
						}

						//UPDATE: Now when we are activating closed, we also assign the max TS in batch(per Symbol)
						_batch_state.put(i.batch_ID, BatchInternalSchema(closed_status = 1,
																		 is_lookup = i.is_lookup_symbol,
																		 lookup_size = i.lookup_size,
																		 latest_ts = i.timestamp,
																		 sec_type = i.sec_type))

						CloseWindows(latest_completed = SafeToReportBatchId(i.symbol, _max_batch_id.value(), out), i = i, out = out)
					case (true, _) =>
						/*
							Initialization if there is not the desired window. Aligns everything to the correct place and assigns the starting window time
						*/

						_window_state.put(current_window_group, WindowInternalSchema(window_group = current_window_group,
																					 last_timestamp = i.timestamp,
																					 last_price = i.price,
																					 affected_batches = Map(i.batch_ID -> true)))


						//UPDATE: Now when we are activating closed, we also assign the max TS in batch(per Symbol)
						_batch_state.put(i.batch_ID, BatchInternalSchema(closed_status = 1,
																		 is_lookup = i.is_lookup_symbol,
																		 lookup_size = i.lookup_size,
																		 latest_ts = i.timestamp,
																		 sec_type = i.sec_type))

						CloseWindows(latest_completed = SafeToReportBatchId(i.symbol, _max_batch_id.value(), out), i = i, out = out)
				}

		}

	}


	/*
		Use this to extract from current timestamp, the window that it belongs in a 5-minute window. It includes entries from [0,5)
	*/
	private def WindowGrouping(current_ts: Long): Long = {
		/*
			This is used in order to add 5 minutes to current long TS
			Formal expression: f(time) = math.floor(time/interval) * interval
		*/
		math.floor(current_ts / (5 * 60 * 1000)).toLong * (5 * 60 * 1000)
	}


	/**
	 * Function that iterates through all the wait batches and initializes them as not close in case they don't exist
   *
	 * @param wait_batches_id
	 */
	private def MarkExpectingBatches(wait_batches_id: Vector[Long]): Unit = {

		@tailrec
		def MarkIterator(wait_batch: Vector[Long]): Unit = {
			wait_batch match {
				case head +: tail =>
					if (!_batch_state.contains(head)) {
						_batch_state.put(head, BatchInternalSchema(closed_status = 0))
					}
					MarkIterator(tail)
				case _ =>
			}
		}

		MarkIterator(wait_batches_id)
	}

	private def SafeToReportBatchId(symbol: String,
									completed_batch_id: Long,
									output: Collector[LastPriceOutputSchema]): (Option[Long], Long) = {

		//safe_to_report -> latest existing batch Id in Symbol which is safe
		//completed_safe -> loop until this batch Id and and all previous either are safe_to_report or don't exist
		var safe_to_report: Option[Long] = None
		var completed_safe: Option[Long] = None
		var tmp_reported_ts: Long = _reported_ts.value()

		var j: Long = _completed_batch.value() + 1
		var must_break: Boolean = false

		while (j <= completed_batch_id && !must_break) {
			val current_batch: Option[BatchInternalSchema] = Option(_batch_state.get(j))

			current_batch match {
				case Some(value) =>
					//If the value is not closed then break since we are expecting batches

					value.closed_status match {
						case 0 => must_break = true
						case 1 =>
							safe_to_report = Some(j)
							tmp_reported_ts = value.latest_ts
						case 2 =>
							if (tmp_reported_ts != -1) {

								val current_window_group = WindowGrouping(current_ts = tmp_reported_ts)
								val cur_window_state: WindowInternalSchema = _window_state.get(current_window_group)

								_window_state.put(current_window_group, cur_window_state
									.copy(affected_batches = cur_window_state.affected_batches + (j -> true)))

								safe_to_report = Some(j)
							} else {
								//This is for the case that a symbol is lookup without having encountered for the first time
								output.collect(LastPriceOutputSchema(symbol = symbol,
																	 sec_type = value.sec_type,
																	 batch_id = j,
																	 is_lookup = true,
																	 lookup_size = value.lookup_size,
																	 window_last_ts = value.latest_ts,
																	 window_closing_price = -1.0,
																	 isLastWindow = true))
							}
					}
				case None =>
			}
			completed_safe = Some(j)
			j += 1
		}

		if (completed_safe.isDefined) {
			_completed_batch.update(completed_safe.get - 1)
			if (safe_to_report.isDefined) _reported_ts.update(tmp_reported_ts)
		}


		(safe_to_report, tmp_reported_ts)
	}



	private def AffectedBatches(latestCompleted: Long,
								reportWindowState: Vector[Long]): Vector[Long] = {
		reportWindowState.filter(_ <= latestCompleted).sorted
	}



	private def CloseWindows(latest_completed: (Option[Long], Long),
							 i: EventUnpackSchema,
						     out: Collector[LastPriceOutputSchema]): Unit = {

		latest_completed._1 match {
			case Some(safe_id) =>
				/*
					When there is a new latest completed value start performing actions
				*/

				val max_window: Long = WindowGrouping(current_ts = latest_completed._2)
				val window_state_iterator = _window_state.keys().iterator()
				//Initialize an empty vector build
				val window_vector_builder = Vector.newBuilder[Long]

				while (window_state_iterator.hasNext) {
					val cur_window = window_state_iterator.next()
					//Add to the list all the windows that do not exceed the max window, including the max window
					if (cur_window <= max_window) {
						window_vector_builder += cur_window
					}
				}



				/*
				This happens when there are windows that are ready to close and have all their batches completed
				*/

				//An array containing all the windows that are safe-to-close
				val completed_windows: Vector[Long] = window_vector_builder.result().sorted


				var prev_state: Option[WindowInternalSchema] = None

				val completed_window_iterator = completed_windows.iterator
				// We are certain there is at least one window since we include the one with the max_window
				var completed_last_window: Boolean = false
				var completed_head: Boolean = true

				while (!completed_last_window) {
					val elem: Long = completed_window_iterator.next()
					val tempWindowState: WindowInternalSchema = _window_state.get(elem)


					// All the affected batches of the completed window should be done
					val affected_batches_window: Map[Long, Boolean] = tempWindowState.affected_batches
					val affected_batches: Vector[Long] = AffectedBatches(safe_id, tempWindowState.affected_batches.keys.toVector)
					affected_batches.foreach(seq => {
						val tempBatchStateSeq: BatchInternalSchema = _batch_state.get(seq)

						if (!completed_head) {
							out.collect(LastPriceOutputSchema(symbol = i.symbol,
															 sec_type = tempBatchStateSeq.sec_type,
															 batch_id = seq,
															 is_lookup = tempBatchStateSeq.is_lookup,
															 lookup_size = tempBatchStateSeq.lookup_size,
															 window_last_ts = prev_state.get.last_timestamp,
															 window_closing_price = prev_state.get.last_price,
														     isLastWindow = affected_batches_window(seq)))
							completed_head = true
						} else if (tempBatchStateSeq.is_lookup) {
							out.collect(LastPriceOutputSchema(symbol = i.symbol,
															  sec_type = tempBatchStateSeq.sec_type,
							   								  batch_id = seq,
															  is_lookup = tempBatchStateSeq.is_lookup,
														      lookup_size = tempBatchStateSeq.lookup_size,
															  window_last_ts = tempWindowState.last_timestamp,
															  window_closing_price = -1.0,
															  isLastWindow = affected_batches_window(seq)))

						}
					})

					if (completed_window_iterator.hasNext) {
						/*
						Remove all the completed window and mark that the batch hasSent
						*/
						prev_state = Some(tempWindowState)
						_window_state.remove(elem) //Once calculated everything discard that window
						completed_head = false
					} else {
						// In case we are in the last window, remove affected batches in order to avoid duplicates in the following iterations
						_window_state.put(elem, tempWindowState.copy(affected_batches = affected_batches_window.filterKeys(x => !affected_batches.contains(x))))
						completed_last_window = true
					}


				}
			case _ =>

		}


	}

}