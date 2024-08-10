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

import Schemas.{CrossoverToReporterSchema, EMASchema}
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.api.java.functions.FunctionAnnotation.ForwardedFields
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`

@ForwardedFields(Array("0", "1", "2", "4"))
class CrossoverCalculator extends KeyedProcessFunction[String, EMASchema, CrossoverToReporterSchema] {

  @transient private var _element_counter: ValueState[Int] = _
  @transient private var _prev_ema: ValueState[(Double, Double)] = _
  @transient private var _bull_bear_registry: MapState[Long, Int] = _


  @throws[Exception]
  override def open(parameters: Configuration): Unit = {
    _element_counter = getRuntimeContext.getState(new ValueStateDescriptor("_element_counter", classOf[Int]))
    _prev_ema = getRuntimeContext.getState(new ValueStateDescriptor("_prev_ema_j", classOf[(Double, Double)], (0.0, 0.0)))
    _bull_bear_registry = getRuntimeContext.getMapState(new MapStateDescriptor[Long, Int]("_bull_bear_registry", classOf[Long], classOf[Int]))
  }

  override def processElement(in: EMASchema,
                              ctx: KeyedProcessFunction[String, EMASchema, CrossoverToReporterSchema]#Context,
                              out: Collector[CrossoverToReporterSchema]): Unit = {

    /**
     * Enumeration for Crossover events:
     *  - 0 -> For Bullish
     *  - 1 -> For Bearish
     */


    if (in.window_closing_price != -1.0) {
      /*
        We consider that ema_j is smaller(38) than ema_w(100)
     */
      val last_ema = _prev_ema.value()
      val last_ema_j = last_ema._1
      val last_ema_w = last_ema._2

      if (last_ema_j != 0.0) {
          _prev_ema.update((in.ema_j, in.ema_w))

        if ((last_ema_j <= last_ema_w) && (in.ema_j > in.ema_w)) {
          UpdateHistory(in.window_last_ts, 0)
          //println(s"Bullish Breakout for key ${in.symbol}, PREVIOUS[j ${_prev_ema_j.value()}, w ${_prev_ema_w.value()}] AND CURRENT [j ${in.ema_j}, w ${in.ema_w}] ")
        }

        if ((last_ema_j >= last_ema_w) && (in.ema_j < in.ema_w)) {
          UpdateHistory(in.window_last_ts, 1)
          //println(s"Bearish Breakout for key ${in.symbol}, PREVIOUS[j ${_prev_ema_j.value()}, w ${_prev_ema_w.value()}] AND CURRENT [j ${in.ema_j}, w ${in.ema_w}] ")
        }

      } else {
        _prev_ema.update((in.ema_j, in.ema_w))
      }
    }


    //Only if we have lookup and is the last window continue to the next operator
    if(in.is_lookup && in.isLastWindow)
      out.collect(CrossoverToReporterSchema(symbol = in.symbol,
                                            sec_type = in.sec_type,
                                            batch_id = in.batch_ID,
                                            is_lookup = true,
                                            lookup_size = in.lookup_size,
                                            isLastWindow = true,
                                            cross_domain_ts = _bull_bear_registry.keys().toList,
                                            cross_domain_mode = _bull_bear_registry.values().toList))


  }



  private def UpdateHistory(element: Long,
                            mode: Int): Unit = {

    val counter: Int = _element_counter.value()

    /**
     * If the history exceeds the last three delete the oldest element
     */
    if (counter == 3)
      _bull_bear_registry.remove(_bull_bear_registry.keys.min)
    else
      _element_counter.update(counter + 1)

    _bull_bear_registry.put(element, mode)


  }

}