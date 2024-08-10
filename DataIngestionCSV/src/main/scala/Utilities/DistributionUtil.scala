package Utilities

import Schemas.{DistrubutionEvent, EnrichedEvent}
import grpc.modules.{Batch, Event}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state._
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.CollectionUtil.iterableToList
import org.apache.flink.util.Collector

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import scala.util.Random

class DistributionUtil(batchSize: Long = 1000) extends RichFlatMapFunction[EnrichedEvent, DistrubutionEvent]{

  @transient private var eventCounter: ValueState[Long] = _
  @transient private var uniqueSymbolList: MapState[String, Long] = _

  /**
   * Function responsible to update the registry of entries. Updates the frequency of each symbols
   * @param curSymbol Current Input Symbol
   */
  def uniqueSymbolRegistry(curSymbol: String): Unit = {
    val symbolCounter: Long = uniqueSymbolList.get(curSymbol)

    if(symbolCounter == 0){
      /*
        In case we see the symbol for the first time, then put it in the registry and put value 1
      */
      uniqueSymbolList.put(curSymbol, 1)
    }else{
      /*
      In case the symbol already exists, in the registry update the counter of occurrences
       */
      uniqueSymbolList.put(curSymbol, symbolCounter+1)
    }
  }


  override def open(parameters: Configuration): Unit = {
    eventCounter = getRuntimeContext.getState(new ValueStateDescriptor("eventCounter", classOf[Long]))
    uniqueSymbolList = getRuntimeContext.getMapState(new MapStateDescriptor[String, Long]("uniqueSymbolList", classOf[String], classOf[Long]))
  }

  override def flatMap(in: EnrichedEvent, collector: Collector[DistrubutionEvent]): Unit = {


      /*
      Once we reach the end of the stream, starting printing the distribution of the symbols
       */

      if(in.event.getSymbol.compareTo("-1") == 0 ){ //eventCounter.value() == 999 * batchSize || in.event.getSymbol.compareTo("-1") == 0
        println(s"END OF THE STREAM in batch ${eventCounter.value()/batchSize}")

        val iterator = uniqueSymbolList.iterator()
        while(iterator.hasNext){
          var tempIter = iterator.next()
          collector.collect(DistrubutionEvent(tempIter.getKey, tempIter.getValue))
        }
      }

      uniqueSymbolRegistry(in.event.getSymbol)
      eventCounter.update(eventCounter.value()+1)

  }
}
