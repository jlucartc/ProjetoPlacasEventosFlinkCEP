import java.util

import org.apache.flink.cep.functions.{PatternProcessFunction, TimedOutPartialMatchHandler}
import org.apache.flink.util.Collector

class Evento1PatternProcessFunction() extends PatternProcessFunction[(String,Double,Double,String,Int,Int),String] with TimedOutPartialMatchHandler[(String,Double,Double,String,Int,Int)]{
    
    override def processMatch(events: util.Map[String, util.List[(String,Double,Double,String,Int,Int)]], ctx: PatternProcessFunction.Context, out: Collector[String]): Unit = {
        
        events.get("evento1").forEach( value => { println("Follow event: "+value._1); out.collect(value._6.toString); })
        
    }
    
    override def processTimedOutMatch(events: util.Map[String, util.List[(String, Double, Double, String, Int, Int)]], ctx: PatternProcessFunction.Context): Unit = {
    
        events.get("evento1").forEach( value => { println("Rejected event: "+value._1) } )
        
    }
    
}
