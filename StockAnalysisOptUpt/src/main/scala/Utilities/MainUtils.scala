package Utilities

import scala.util.Try

object MainUtils {
  def parameterParser(arguments: Array[String], parallel: Int): (Double, Double, Int, String) = {
    println(s"++++++Application Parameters++++++++")

    val chkLocation: String = arguments(3)

    val checkpointInterval: Int = if (Try(arguments(2).toInt).isSuccess) {
      arguments(2).toInt
    } else {
      -1
    }

    var j1: Double = 0.0
    var j2: Double = 0.0
    try {
      //j1 must be always smaller than the j2
      if (arguments(0).toDouble < arguments(1).toDouble) {
        j1 = arguments(0).toDouble
        j2 = arguments(1).toDouble
      } else {
        j1 = arguments(1).toDouble
        j2 = arguments(0).toDouble
      }

    } catch {
      case e: NumberFormatException => {
        //In case of an error give the default values
        j1 = 38
        j2 = 100
      }
    } finally {
      println(s"\n\tParallelism: ${parallel} \n\tj1: ${j1}\n\tj2: ${j2}\n\tCheckpoint: ${
        if (checkpointInterval == -1.0) {
          "No Checkpoint"
        } else {
          checkpointInterval.toString + " minutes" + " in path " + chkLocation
        }
      }")
    }

    println("\n++++++++++++++++++++++++++++++++++++")
    (j1, j2, checkpointInterval, chkLocation)

  }
}
