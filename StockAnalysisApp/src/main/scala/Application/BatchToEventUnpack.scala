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

import Schemas.EventUnpackSchema
import com.google.protobuf.Timestamp
import com.google.protobuf.util.Timestamps
import grpc.modules.{Batch, SecurityType}
import org.apache.flink.api.common.functions.{FlatMapFunction, RichFlatMapFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.{Meter, MeterView}
import org.apache.flink.util.Collector

import java.time.LocalDateTime
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.collection.mutable.HashMap

class BatchToEventUnpack extends RichFlatMapFunction[Batch, EventUnpackSchema] {

  @transient private var meter: Meter = _

  override def open(parameters: Configuration): Unit = {
    meter = getRuntimeContext.getMetricGroup().meter("myMeter", new MeterView(1))
  }

  override def flatMap(batch: Batch, collector: Collector[EventUnpackSchema]): Unit = {
    meter.markEvent()
    val lookupSymbols: Set[String] = batch.getLookupSymbolsList.toSet

    println(s"UNPACK START [Batch ID: ${batch.getSeqId}, Timestamp: ${LocalDateTime.now()}, EventSize: ${batch.getEventsList.size() + lookupSymbols.size}], Rate: ${meter.getRate}")


    /**
     * Preprocessing step involves scanning all the events once in a batch to find:
     *  - Total Timestamp of batch
     *  - Last in Bi
     *  - If a symbol is lookup
     *  - The number of lookup symbols
     */

    var distinctEventMap: HashMap[String, Int] = HashMap.empty[String, Int]


    var eventCounter: Int = 0
    batch.getEventsList.forEach(event => {
      distinctEventMap(event.getSymbol) = eventCounter
      eventCounter += 1
    })

    /**
     * In case one of the lookupSymbols does not exist in the batch, then add a dummy value to get the correct results
     */
    lookupSymbols.foreach(ls => {
      if (!distinctEventMap.contains(ls)) {
        collector.collect(EventUnpackSchema(Symbol = ls,
                                            securityType = SecurityType.Equity,
                                            Price = -1.0,
                                            Timestamp = Timestamps.toMillis(batch.getEventsList.get(0).getLastTrade),
                                            batchID = batch.getSeqId,
                                            totalBatchTimestamp = Timestamps.toMillis(batch.getEventsList.get(0).getLastTrade),
                                            LastInBiBool = true,
                                            lookupSymbolBool = true,
                                            lookupSize = lookupSymbols.size,
                                            isLastBatch = batch.getLast))
      }

    })


    var newEventCounter: Int = 0
    batch.getEventsList.forEach(b => {

      val lastInBi: Boolean = if (newEventCounter == distinctEventMap.getOrElse(b.getSymbol, 0).toInt) {
        true
      } else {
        false
      }

      collector.collect(EventUnpackSchema(Symbol = b.getSymbol,
                                          securityType = b.getSecurityType,
                                          Price = b.getLastTradePrice,
                                          Timestamp = Timestamps.toMillis(b.getLastTrade),
                                          batchID = batch.getSeqId,
                                          totalBatchTimestamp = Timestamps.toMillis(batch.getEventsList.get(distinctEventMap.getOrElse(b.getSymbol, 0)).getLastTrade),
                                          LastInBiBool = lastInBi,
                                          lookupSymbolBool = lookupSymbols.contains(b.getSymbol),
                                          lookupSize = lookupSymbols.size,
                                          isLastBatch = batch.getLast))

      newEventCounter += 1

    }
    )
  }
}
