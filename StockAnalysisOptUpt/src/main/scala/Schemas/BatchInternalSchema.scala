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

package Schemas

import grpc.modules.SecurityType


/*
  Closed Status Code:
   -  0 on initialization
   -  1 on closed
   -  2 on only_lookup
 */
final case class BatchInternalSchema(closed_status: Byte,
                                     is_lookup: Boolean = false,
                                     lookup_size: Int = -1,
                                     latest_ts: Long = 0L,
                                     sec_type: SecurityType = SecurityType.Equity) {
//  def _ActivateHasSent: BatchInternalSchema = this.copy(hasSent = true)

//  def _ActivateIsClosed(totalTsInBatch: Long) : BatchInternalSchema = this.copy(is_closed = true, latest_ts = totalTsInBatch)

  }