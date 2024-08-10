package Application

import Schemas.EnrichedEvent
import Utilities.TimestampUtil
import Utilities.TimestampUtil.toLocalDate
import grpc.modules.{Batch, Event}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, MapState, MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.CollectionUtil.iterableToList
import org.apache.flink.util.Collector

import scala.collection.JavaConverters._
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random

class EventToBatch(batchSize: Long,
                   lookupSize: Int,
                   throttler: Int) extends RichFlatMapFunction[EnrichedEvent, Batch]{

  @transient private var _event_counter: ValueState[Long] = _
  @transient private var _cur_batch_id: ValueState[Long] = _
  @transient private var _total_batch_events: ListState[Event] = _
  @transient private var _unique_symbol_registry: MapState[String, Long] = _

  override def open(parameters: Configuration): Unit = {
    _event_counter = getRuntimeContext.getState(new ValueStateDescriptor("_event_counter", classOf[Long]))
    _cur_batch_id = getRuntimeContext.getState(new ValueStateDescriptor("_cur_batch_id", classOf[Long]))
    _total_batch_events = getRuntimeContext.getListState(new ListStateDescriptor("_total_batch_events", classOf[Event]))
    _unique_symbol_registry = getRuntimeContext.getMapState(new MapStateDescriptor[String, Long]("_unique_symbol_registry", classOf[String], classOf[Long]))
  }

  override def flatMap(in: EnrichedEvent, collector: Collector[Batch]): Unit = {

    //Count the total events and divide them by batch size to form the batch in the desired size

    val new_batch_id: Long = _event_counter.value()/batchSize
    val current_batch_id: Long = _cur_batch_id.value()

    /*
      Don't exceed the throttler
     */
    if(new_batch_id < throttler){

      /*
      This is the case of the last batch to emit. Simply emit the results
       */
      if(in.event.getSymbol.compareTo("-1") == 0){
        println("LAST BATCH TO EMIT")


        collector.collect(BatchBuilder(cur_batch_id = current_batch_id, is_last = true))

        _total_batch_events.clear()
      }

      UniqueSymbolRegistry(in.event.getSymbol)
      _event_counter.update(_event_counter.value() + 1)

      //Check the Batch ID and if changed then emit results for the previous batch
      if (current_batch_id == new_batch_id){
        _total_batch_events.add(in.event)
      }else{

        val batch_result: Batch = BatchBuilder(cur_batch_id = current_batch_id, is_last = false)

        batch_result.getEventsList.forEach(x => println(s"FOR BATCH ID ${batch_result.getSeqId} SYMBOL ${x.getSymbol}, PRICE: ${x.getLastTradePrice}, TS ${TimestampUtil.toLocalDate(x.getLastTrade)}"))
        collector.collect(batch_result)

        //After adding the events to the batch, clear the list
        _total_batch_events.clear()

        //Add the new event to the list and change the current batchID
        _total_batch_events.add(in.event)
        _cur_batch_id.update(new_batch_id)

      }

    }

  }


  /**
   * Function responsible to update the registry of entries. Updates the frequency of each symbols
   *
   * @param curSymbol Current Input Symbol
   */
  private def UniqueSymbolRegistry(curSymbol: String): Unit = {
    val symbolCounter: Long = _unique_symbol_registry.get(curSymbol)

    if (symbolCounter == 0) {
      /*
        In case we see the symbol for the first time, then put it in the registry and put value 1
      */
      _unique_symbol_registry.put(curSymbol, 1)
    } else {
      /*
      In case the symbol already exists, in the registry update the counter of occurrences
       */
      _unique_symbol_registry.put(curSymbol, symbolCounter + 1)
    }
  }

  private def LookupSymbolPicker(): java.lang.Iterable[String] = {

    val registryFull = iterableToList(_unique_symbol_registry.keys()).toArray
    val registrySize: Int = registryFull.size
    val seed: Long = _cur_batch_id.value()

    val r1: Random = new scala.util.Random(seed) //Give as seed the batchID to generate each time different random results
    var lookupList: ListBuffer[String] = new ListBuffer[String]()

    for (i <- 0 until Math.min(lookupSize, registrySize)) {
      var randChoice: Int = r1.nextInt(registrySize)
      /*
      Add the chosen random entry to the lookupList
       */
      lookupList += registryFull(randChoice).asInstanceOf[String]
    }

    lookupList.asJava //Cast to Array and return

  }

  private def BatchBuilder(cur_batch_id: Long, is_last: Boolean): Batch = {
    Batch.newBuilder()
          .setSeqId(cur_batch_id)
          .addAllEvents(_total_batch_events.get())
          .addAllLookupSymbols(LookupSymbolPicker())
          .setLast(is_last)
          .build()
  }
}
