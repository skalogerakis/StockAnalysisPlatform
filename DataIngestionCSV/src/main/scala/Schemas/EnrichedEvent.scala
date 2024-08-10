package Schemas

import grpc.modules.Event

case class EnrichedEvent(event: Event, CountEntry: Long)
