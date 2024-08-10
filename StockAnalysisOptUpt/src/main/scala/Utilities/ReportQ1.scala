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

import Schemas.EMASchema
import grpc.modules.{Indicator, ResultQ1}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector

import java.time.LocalDateTime

class ReportQ1 extends KeyedProcessFunction[Long, EMASchema, ResultQ1] {

  @transient private var _event_counter_q1: ValueState[Int] = _
  @transient private var _result_list_q1: ListState[Indicator] = _

  @throws[Exception]
  override def open(parameters: Configuration): Unit = {
    _event_counter_q1 = getRuntimeContext.getState(new ValueStateDescriptor("event_counter_q1", classOf[Int]))
    _result_list_q1 = getRuntimeContext.getListState(new ListStateDescriptor[Indicator]("_result_list_q1", classOf[Indicator]))
  }


  override def processElement(in: EMASchema,
                              ctx: KeyedProcessFunction[Long, EMASchema, ResultQ1]#Context,
                              out: Collector[ResultQ1]): Unit = {

    val increment: Int = _event_counter_q1.value() + 1

        _result_list_q1.add(IndicatorConstructor(symbol = in.symbol,
                                                  ema_w = in.ema_w.toFloat,
                                                  ema_j = in.ema_j.toFloat))

        //In case we have found all the required elements print them
        if (increment == in.lookup_size) {
          println(s"REPORT Q1: [Batch ID: ${in.batch_ID}, Timestamp: ${LocalDateTime.now()}],")
          out.collect(ResultConstructor(ctx.getCurrentKey))
          _event_counter_q1.update(increment + 1)
          _result_list_q1.clear()
        } else {
          _event_counter_q1.update(increment)
        }



  }

  private def ResultConstructor(batch_id: Long): ResultQ1 = {
    ResultQ1.newBuilder()
            .setBatchSeqId(batch_id)
            .addAllIndicators(_result_list_q1.get())
            .build()

  }

  private def IndicatorConstructor(symbol: String,
                                   ema_w: Float,
                                   ema_j: Float): Indicator = {
    Indicator.newBuilder()
              .setSymbol(symbol)
              .setEma38(ema_j)
              .setEma100(ema_w)
              .build()
  }

}

