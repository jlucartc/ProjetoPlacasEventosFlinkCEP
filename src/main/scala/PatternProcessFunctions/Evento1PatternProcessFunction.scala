package PatternProcessFunctions

import java.util

import org.apache.flink.cep.functions.PatternProcessFunction
import org.apache.flink.util.Collector

class Evento1PatternProcessFunction() extends PatternProcessFunction[(Int,Int,String,String,Long),String]{
    
    override def processMatch(events: util.Map[String, util.List[(Int,Int,String,String,Long)]], ctx: PatternProcessFunction.Context, out: Collector[String]): Unit = {
        
        events.get("evento1").forEach( value => { println("Follow event: "+value._1); out.collect("("+value._3+","+value._4+","+value._5+")"); })
        
    }
    
}
