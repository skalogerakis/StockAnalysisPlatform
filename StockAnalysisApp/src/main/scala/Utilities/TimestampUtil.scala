package Utilities

import com.google.protobuf.Timestamp
import com.google.protobuf.util.Timestamps

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneId}

object TimestampUtil {

  def toLongDate(timestamp: Timestamp): Long ={
    Timestamps.toMillis(timestamp)
  }

  def toLocalDate(timestamp: Timestamp): String = {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS")
    LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds, timestamp.getNanos), ZoneId.systemDefault).format(formatter)
  }

  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) + "ns")
    result
  }
}
