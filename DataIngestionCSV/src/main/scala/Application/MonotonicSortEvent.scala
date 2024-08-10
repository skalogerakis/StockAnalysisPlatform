package Application

import Schemas.EnrichedEvent
import Utilities.TimestampUtil
import com.google.protobuf.util.Timestamps
import grpc.modules.{Batch, Event}
import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state._
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.CollectionUtil.{iterableToList, iteratorToList}
import org.apache.flink.util.Collector

import java.util.Comparator
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import scala.util.Random

class MonotonicSortEvent(windowSensitivity: Long = 1) extends RichFlatMapFunction[EnrichedEvent, EnrichedEvent]{

  @transient private var eventCounter: ValueState[Long] = _
  @transient private var lastWindowGroup: ValueState[Long] = _
  @transient private var listWindowEvents: ListState[EnrichedEvent] = _

  /*
      Function used in order group events in time windows. When a new time window is found then sort the events of the previous window and emit them
   */
  def windowGrouping(currentTS: Long): Long = {
    /*
       Formal expression: f(time) = math.floor(time/interval) * interval
     */
    math.floor(currentTS / (windowSensitivity * 60 * 1000)).toLong * (windowSensitivity * 60 * 1000)
  }

  /**
   * Function responsible to update the registry of entries. Updates the frequency of each symbols
   * @param curSymbol Current Input Symbol
   */

  override def open(parameters: Configuration): Unit = {
    eventCounter = getRuntimeContext.getState(new ValueStateDescriptor("eventCounter", classOf[Long]))
    lastWindowGroup = getRuntimeContext.getState(new ValueStateDescriptor("lastWindowGroup", classOf[Long]))
    listWindowEvents = getRuntimeContext.getListState(new ListStateDescriptor("listWindowEvents", classOf[EnrichedEvent]))
  }

  override def flatMap(in: EnrichedEvent, collector: Collector[EnrichedEvent]): Unit = {

    val curWindowGroup: Long = windowGrouping(TimestampUtil.toLongDate(in.event.getLastTrade))

    //On the first event we don't want to emit events, just initialize the first window group
    if(eventCounter.value() == null){
      lastWindowGroup.update(curWindowGroup)
    }

    //When the desired window changes, first sort the entries, then emit the results
    if(curWindowGroup != lastWindowGroup.value() ){

      /*
      Sort everything by Trade Timestamp
       */
      val sortedListEvents = iterableToList(listWindowEvents.get()).toArray.sortBy( x => TimestampUtil.toLongDate(x.asInstanceOf[EnrichedEvent].event.getLastTrade))

      //First clear old state and then add new one
      listWindowEvents.clear()
      lastWindowGroup.update(curWindowGroup)
      listWindowEvents.add(in)

      //Emit all the results from the previous window
      val collectIter = sortedListEvents.toIterator
      while(collectIter.hasNext){
        collector.collect(collectIter.next().asInstanceOf[EnrichedEvent])
      }

      //In case we reach the end of the stream, emit also the dummy entry to the next operator
      if(in.event.getSymbol.compareTo("-1") == 0){
        collector.collect(in)
      }
    }else{
      //No window change, just add the event in the list
      listWindowEvents.add(in)
    }
    eventCounter.update(eventCounter.value()+1)
  }
}
