package Utilities

import com.google.protobuf.Timestamp
import com.google.protobuf.util.Timestamps

import java.time.{Instant, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

object TimestampUtil {

  def toLongDate(timestamp: Timestamp): Long ={
    Timestamps.toMillis(timestamp)
  }

  def toLocalDate(timestamp: Timestamp): String = {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS")
    LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds, timestamp.getNanos), ZoneId.systemDefault).format(formatter)
  }
}
