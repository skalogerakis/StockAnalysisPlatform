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

import Schemas.{EMASchema, LastPriceOutputSchema}
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.api.java.functions.FunctionAnnotation.{ForwardedFields, ReadFields}
import org.apache.flink.configuration.Configuration

@ForwardedFields(Array("0", "1", "2", "3", "4", "5","7")) //Those fields don't contribute in conditional statements or computations
@ReadFields(Array("6")) //Indicate that field WindowClosingPrice is used
class EMACalculator(j: Double = 38.0, w: Double = 100.0) extends RichMapFunction[LastPriceOutputSchema, EMASchema] {

  @transient private var _last_ema: ValueState[(Double,Double)] = _


  override def open(parameters: Configuration): Unit = {
    _last_ema = getRuntimeContext.getState(new ValueStateDescriptor[(Double, Double)]("lastEMAj", classOf[(Double, Double)], (0.0,0.0)))

  }

  override def map(in: LastPriceOutputSchema): EMASchema = {

    /*
      Calculate ema_j and ema_w. The initial value of the ValueState is not null but 0.0 so everything is more simple.
      The first case a new window is not triggered, and simply return the latest EMA prices for reporting in the next stage.
      The second case calculates the new EMA.
     */

    val closing_price: Double = in.window_closing_price
    closing_price match {
      case -1.0 =>
        val last_ema = _last_ema.value()
        EMASchema(symbol = in.symbol,
                  sec_type = in.sec_type,
                  batch_ID = in.batch_id,
                  is_lookup = in.is_lookup,
                  lookup_size = in.lookup_size,
                  window_last_ts = in.window_last_ts,
                  window_closing_price = closing_price,
                  isLastWindow = in.isLastWindow,
                  ema_j = last_ema._1,
                  ema_w = last_ema._2
                  )
      case _ =>

        val last_ema = _last_ema.value()

        val ema_j: Double = EmaEval(closing_price, last_ema._1, j)
        val ema_w: Double = EmaEval(closing_price, last_ema._2, w)
        _last_ema.update((ema_j, ema_w))

        EMASchema(symbol = in.symbol,
                  sec_type = in.sec_type,
                  batch_ID = in.batch_id,
                  is_lookup = in.is_lookup,
                  lookup_size = in.lookup_size,
                  window_last_ts = in.window_last_ts,
                  window_closing_price = closing_price,
                  isLastWindow = in.isLastWindow,
                  ema_j = ema_j,
                  ema_w = ema_w)
    }

  }

  def EmaEval(last_price: Double, last_ema: Double, param: Double): Double = {
    last_price * (2 / (1 + param)) + last_ema * (1 - (2 / (1 + param)))
  }


}
