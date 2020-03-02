import java.util

import org.apache.flink.cep.functions.PatternProcessFunction
import org.apache.flink.util.Collector

class EventoTestePatternProcessFunction extends PatternProcessFunction[(String,Double,Double,String,Int,Int),String] {
    override def processMatch(events: util.Map[String, util.List[(String, Double, Double, String, Int, Int)]], ctx: PatternProcessFunction.Context, out: Collector[String]): Unit = {
        
        events.get("teste").forEach( value => { out.collect(value._6.toString) })
        
    }
}
