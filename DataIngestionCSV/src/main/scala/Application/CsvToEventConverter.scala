package Application

import Schemas.{_EventSchema, InputCSVSchema}
import com.google.protobuf.util.Timestamps
import grpc.modules.{Event, SecurityType}
import org.apache.flink.api.common.functions.RichMapFunction

//Naive implementation for demonstration purposes only
class CsvToEventConverter extends RichMapFunction[InputCSVSchema, Event] {
  private val format = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS") //NOTICE!! BE EXTREMELY CAREFUL WITH THAT

  override def map(in: InputCSVSchema): Event = {
    if (in.ID.compareTo("-1") == 0){
      println("END OF STREAM DETECTED")
      return Event.newBuilder()
                  .setSymbol(in.ID)
                  .setLastTradePrice(-1)
                  .build()
    }

    val securityType: Int = in.SecType match {
      case "I" => 1
      case "E" => 0
      case _ => -1
    }

    // In case there is not security type, don't set it to avoid exceptions
    if (securityType == -1) {

      Event.newBuilder()
        .setSymbol(in.ID)
        .setLastTrade(Timestamps.fromMillis(format.parse(in.Date + " " + in.Trading_Time).getTime()))
        .setLastTradePrice(in.Last.toDouble.toFloat)
        .build()
    } else {

      Event.newBuilder()
        .setSymbol(in.ID)
        .setSecurityType(SecurityType.forNumber(securityType))
        .setLastTrade(Timestamps.fromMillis(format.parse(in.Date + " " + in.Trading_Time).getTime()))
        .setLastTradePrice(in.Last.toDouble.toFloat)
        .build()
    }


  }
}



