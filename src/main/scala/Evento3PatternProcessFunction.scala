import java.util

import org.apache.flink.cep.functions.PatternProcessFunction
import org.apache.flink.util.Collector

class Evento3PatternProcessFunction() extends PatternProcessFunction[(String,Double,Double,String,Int,Int),String]{
    
    override def processMatch(events: util.Map[String, util.List[(String, Double, Double, String, Int, Int)]], ctx: PatternProcessFunction.Context, out: Collector[String]): Unit = {
        
        events.get("evento3").forEach( value => { println("Evento3: "+value._1); out.collect(value._6.toString); })
        
    }
}
