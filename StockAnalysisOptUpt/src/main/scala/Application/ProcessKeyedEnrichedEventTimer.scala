package Application

import Schemas.{EventEnrichmentSchema, EventUnpackSchema}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.util.Collector

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.HashMap

//Key, Input, Broadcasted, Output
class ProcessKeyedEnrichedEventTimer(timerTriggerInterval: Long = 500L) extends KeyedBroadcastProcessFunction[String, EventEnrichmentSchema, (Long, String), EventUnpackSchema] {

  private var _completed_registry: mutable.ArrayBuffer[Long] = mutable.ArrayBuffer.empty[Long]
  private var _last_in_sequence_batch: Long = -1L
  private var _batches_wait_reg: mutable.HashMap[String, Vector[Long]] = HashMap.empty[String, Vector[Long]]

  @transient private var _timer: ValueState[Long] = _
  @transient private var _max_batch: ValueState[Long] = _

  override def open(parameters: Configuration): Unit = {
    _timer = getRuntimeContext.getState(new ValueStateDescriptor("_timer", classOf[Long]))
    _max_batch = getRuntimeContext.getState(new ValueStateDescriptor("_max_batch_enr", classOf[Long]))
  }




  override def processElement(event: EventEnrichmentSchema,
                              readOnlyContext: KeyedBroadcastProcessFunction[String, EventEnrichmentSchema, (Long, String), EventUnpackSchema]#ReadOnlyContext,
                              out: Collector[EventUnpackSchema]): Unit = {

    //Emit all the events in the next operator
    out.collect(EventUnpackSchema(symbol = event.symbol,
                                  sec_type = event.sec_type,
                                  price = event.price,
                                  timestamp = event.timestamp,
                                  batch_ID = event.batch_ID,
                                  last_symbol_in_batch = event.last_symbol_in_batch,
                                  is_lookup_symbol = event.is_lookup_symbol,
                                  lookup_size = event.lookup_size))


    if (event.last_symbol_in_batch){
//      _timer.clear()

      var max_val = _max_batch.value()

      //update the max value seen by this key
      if(event.batch_ID > max_val) {
        _max_batch.update(event.batch_ID)
        max_val = event.batch_ID
      }

      val wait_bt: Vector[Long] = _batches_wait_reg.withDefaultValue(Vector.empty[Long])(event.symbol)

      if (wait_bt.nonEmpty) {

        out.collect(EventUnpackSchema(symbol = event.symbol,
                                      price = -2.0,
                                      batch_ID = _last_in_sequence_batch,
                                      last_symbol_in_batch = true,
                                      lookup_size = -1,
                                      wait_batches = Some(wait_bt)))

        _batches_wait_reg.remove(event.symbol)

        val timer = readOnlyContext.timerService.currentProcessingTime + timerTriggerInterval
        readOnlyContext.timerService.registerProcessingTimeTimer(timer)
        _timer.update(timer)

      } else if (_last_in_sequence_batch >= max_val) {
        out.collect(EventUnpackSchema(symbol = event.symbol,
                                      price = -2.0,
                                      batch_ID = _last_in_sequence_batch,
                                      last_symbol_in_batch = true,
                                      lookup_size = -1,
                                      wait_batches = Some(wait_bt)))

        _batches_wait_reg.remove(event.symbol)

      } else {
        val timer = readOnlyContext.timerService.currentProcessingTime + timerTriggerInterval
        readOnlyContext.timerService.registerProcessingTimeTimer(timer)
        _timer.update(timer)
      }

    }


  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedBroadcastProcessFunction[String, EventEnrichmentSchema, (Long, String), EventUnpackSchema]#OnTimerContext,
                       out: Collector[EventUnpackSchema]): Unit = {

    val max_val = _max_batch.value()
    val symbol = ctx.getCurrentKey
    val wait_bt: Vector[Long] = _batches_wait_reg.withDefaultValue(Vector.empty[Long])(symbol)

    if (wait_bt.nonEmpty ) {
      out.collect(EventUnpackSchema(symbol = symbol,
                                    price = -2.0,
                                    batch_ID = _last_in_sequence_batch,
                                    last_symbol_in_batch = true,
                                    lookup_size = -1,
                                    wait_batches = Some(wait_bt)))

      _batches_wait_reg.remove(symbol)

      val timer = ctx.timerService.currentProcessingTime + timerTriggerInterval
      ctx.timerService.registerProcessingTimeTimer(timer)
      _timer.update(timer)

    } else if (_last_in_sequence_batch >= max_val) {
      out.collect(EventUnpackSchema(symbol = symbol,
                                    price = -2.0,
                                    batch_ID = _last_in_sequence_batch,
                                    last_symbol_in_batch = true,
                                    lookup_size = -1,
                                    wait_batches = Some(wait_bt)))

      _batches_wait_reg.remove(symbol)

    } else {
      val timer = ctx.timerService.currentProcessingTime + timerTriggerInterval
      ctx.timerService.registerProcessingTimeTimer(timer)
      _timer.update(timer)
    }



  }


  override def processBroadcastElement(br: (Long, String),
                                       ctx: KeyedBroadcastProcessFunction[String, EventEnrichmentSchema, (Long, String), EventUnpackSchema]#Context,
                                       out: Collector[EventUnpackSchema]): Unit = {

    //For now when broadcast value changes and update the same time the safeToReportBatch

    br._2.split("_").foreach(sym => {
      _batches_wait_reg(sym) = _batches_wait_reg.withDefaultValue(Vector.empty[Long])(sym) :+ br._1
    })

    //Find the safe_to_Complete batch from the broadcast registry
    _completed_registry += br._1

    var cur_safe: Long = _last_in_sequence_batch + 1
    while(_completed_registry.contains(cur_safe)){
      _completed_registry -= cur_safe
      cur_safe += 1
    }


    _last_in_sequence_batch = cur_safe - 1
  }




}