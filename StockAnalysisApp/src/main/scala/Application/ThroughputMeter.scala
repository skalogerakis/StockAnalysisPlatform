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
import com.google.protobuf.util.Timestamps
import grpc.modules.{Batch, SecurityType}
import org.apache.flink.api.common.functions.{FlatMapFunction, RichFlatMapFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.metrics.{Meter, MeterView}
import org.apache.flink.util.Collector

import java.time.LocalDateTime
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.collection.mutable.HashMap

class ThroughputMeter extends RichFlatMapFunction[Batch, Unit] {

  var received = 0
  val logfreq = 10
  var lastLog: Long = -1
  var lastElements = 0
  var freq_counter = -1
  var avg_lat: Double = 0
  var uuid = ""

  override def open(parameters: Configuration): Unit = {
    uuid = java.util.UUID.randomUUID.toString
  }

  override def flatMap(batch: Batch, collector: Collector[Unit]): Unit = {
    received += 1

    if (received % logfreq == 0) {
      freq_counter += 1
      // throughput over entire time
      val now = System.currentTimeMillis

      // throughput for the last "logfreq" elements
      if (lastLog == -1) {
        // init (the first)
        lastLog = now
        lastElements = received

      } else {

        val timeDiff = now - lastLog
        val elementDiff = received - lastElements
        val ex = 1000 / timeDiff.toDouble // Time is in ms so must convert to second
        
        avg_lat += (elementDiff * ex)
        println(s"UUID: ${uuid} Iter: ${freq_counter}, Time Diff: ${timeDiff}ms, Elements Received: ${elementDiff}, Th/put: ${elementDiff * ex} elements/second/core, AVG Th/put: ${avg_lat/freq_counter} elements/core ")
        // reinit
        lastLog = now
        lastElements = received
      }
    }

  }
}
