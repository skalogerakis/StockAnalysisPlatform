package Application

import Schemas.EventEnrichmentSchema
import com.google.protobuf.util.Timestamps
import grpc.modules.{Batch, Event, SecurityType}
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.{Meter, MeterView}
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{OutputTag, createTypeInformation}
import org.apache.flink.util.Collector

import java.time.LocalDateTime
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.collection.mutable.HashMap

class BatchToEventProcess extends ProcessFunction[Batch, EventEnrichmentSchema] {

  private val _output_tag = OutputTag[(Long, String)]("side-output")

  override def processElement(batch: Batch,
                              context: ProcessFunction[Batch, EventEnrichmentSchema]#Context,
                              collector: Collector[EventEnrichmentSchema]): Unit = {

    val lookup_symbols: Set[String] = batch.getLookupSymbolsList.toSet
    val lookup_symbols_size = lookup_symbols.size
    val current_batch: Long = batch.getSeqId

    println(s"UNPACK START [Batch ID: ${current_batch}, Timestamp: ${LocalDateTime.now()}, EventSize: ${batch.getEventsList.size() + lookup_symbols_size}]")


    /**
     * Preprocessing step involves scanning all the events once in a batch to find:
     *  - Total Timestamp of batch
     *  - Last in Bi
     *  - If a symbol is lookup
     *  - The number of lookup symbols
     */
    var distinct_event_map: HashMap[String, Event] = HashMap.empty[String, Event]


    val list_event_iter = batch.getEventsList.iterator()
    while (list_event_iter.hasNext) {
      val current_event = list_event_iter.next()
      val current_symbol: String = current_event.getSymbol

      //Check if this is the first encounter of this symbol
      distinct_event_map.get(current_symbol) match {
        case Some(value) =>
          //We have seen this symbol, so output the old one and update with the new one
          collector.collect(EventEnrichmentSchema(symbol = current_symbol,
                                                  sec_type = value.getSecurityType,
                                                  price = value.getLastTradePrice,
                                                  timestamp = Timestamps.toMillis(value.getLastTrade),
                                                  batch_ID = current_batch,
                                                  last_symbol_in_batch = false,
                                                  is_lookup_symbol = lookup_symbols.contains(value.getSymbol),
                                                  lookup_size = lookup_symbols_size))

          distinct_event_map(current_symbol) = current_event
        case None =>
          //This is the first encounter of the symbol so add it to the hashmap
          distinct_event_map(current_symbol) = current_event
      }

    }

    //This will output all the last encounters of each Symbol
    val last_encounter_iter = distinct_event_map.iterator
    while (last_encounter_iter.hasNext) {
      val key_val = last_encounter_iter.next()
      val key = key_val._1
      val value = key_val._2
      collector.collect(EventEnrichmentSchema(symbol = key,
                                              sec_type = value.getSecurityType,
                                              price = value.getLastTradePrice,
                                              timestamp = Timestamps.toMillis(value.getLastTrade),
                                              batch_ID = current_batch,
                                              last_symbol_in_batch = true,
                                              is_lookup_symbol = lookup_symbols.contains(value.getSymbol),
                                              lookup_size = lookup_symbols_size))
    }


    val exception_lookup_sb = new StringBuilder


    /**
     * In case one of the lookupSymbols does not exist in the batch, then add a dummy value to get the correct results.
     * Also add in the unique lookupEvents in the complete Distinct Events list
     */
    lookup_symbols.foreach(ls => {
      if (!distinct_event_map.get(ls).isDefined) {

        exception_lookup_sb.append("_" + ls)
        collector.collect(EventEnrichmentSchema(symbol = ls,
                                                sec_type = SecurityType.Equity,
                                                price = -1.0,
                                                timestamp = Timestamps.toMillis(batch.getEventsList.get(0).getLastTrade),
                                                batch_ID = current_batch,
                                                last_symbol_in_batch = true,
                                                is_lookup_symbol = true,
                                                lookup_size = lookup_symbols_size))
      }

    })


    //println(s"UNPACK FINISH [Batch ID: ${current_batch}, Timestamp: ${LocalDateTime.now()}, EventSize: ${batch.getEventsList.size() + lookup_symbols_size}]")


    //Send side-streeam with all the distinct events
    context.output(_output_tag, (current_batch, distinct_event_map.keys.mkString("_") + exception_lookup_sb.toString))
  }

}