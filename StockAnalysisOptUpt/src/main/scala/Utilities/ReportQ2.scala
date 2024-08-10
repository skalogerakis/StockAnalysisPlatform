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

package Utilities

import Schemas.CrossoverToReporterSchema
import com.google.protobuf.util.Timestamps.fromMillis
import grpc.modules.CrossoverEvent.SignalType
import grpc.modules.{CrossoverEvent, ResultQ2, SecurityType}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector

class ReportQ2 extends KeyedProcessFunction[Long, CrossoverToReporterSchema, ResultQ2] {

  @transient private var _event_counter_q2: ValueState[Int] = _
  @transient private var _result_list_q2: ListState[CrossoverEvent] = _

  @throws[Exception]
  override def open(parameters: Configuration): Unit = {
    _event_counter_q2 = getRuntimeContext.getState(new ValueStateDescriptor("_event_counter_q2", classOf[Int]))
    _result_list_q2 = getRuntimeContext.getListState(new ListStateDescriptor[CrossoverEvent]("_result_list_q2", classOf[CrossoverEvent]))
  }


  override def processElement(in: CrossoverToReporterSchema,
                              ctx: KeyedProcessFunction[Long, CrossoverToReporterSchema, ResultQ2]#Context,
                              out: Collector[ResultQ2]): Unit = {

    val increment: Int = _event_counter_q2.value() + 1

        for (i <- in.cross_domain_mode.indices)
          _result_list_q2.add(CrossoverConstructor(symbol = in.symbol,
                                                    sec = in.sec_type,
                                                    ts = in.cross_domain_ts(i),
                                                    mode = in.cross_domain_mode(i)))

        if (increment == in.lookup_size) {

          //println(s"REPORT Q2: [Batch ID: ${in.batchID}, Timestamp ${LocalDateTime.now()}]")
          out.collect(ResultConstructor(ctx.getCurrentKey))

          _event_counter_q2.update(increment + 1)
        }else{
          _event_counter_q2.update(increment)
        }

  }
  
  private def ResultConstructor(batch_id: Long): ResultQ2 = {
    ResultQ2.newBuilder()
            .setBatchSeqId(batch_id)
            .addAllCrossoverEvents(_result_list_q2.get())
            .build()
  }

  private def CrossoverConstructor(symbol: String, sec: SecurityType, ts: Long, mode: Int): CrossoverEvent = {
    CrossoverEvent.newBuilder()
                  .setSymbol(symbol)
                  .setSecurityType(sec)
                  .setSignalType(SignalType.forNumber(mode))
                  .setTs(fromMillis(ts))
                  .build()
  }

}

