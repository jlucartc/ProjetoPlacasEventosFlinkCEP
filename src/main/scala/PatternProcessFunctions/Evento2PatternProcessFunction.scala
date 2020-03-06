package PatternProcessFunctions

import java.util

import org.apache.flink.cep.functions.PatternProcessFunction
import org.apache.flink.util.Collector

class Evento2PatternProcessFunction extends PatternProcessFunction[(String,Double,Double,String,Int,Int),String]{
    override def processMatch(events: util.Map[String, util.List[(String, Double, Double, String, Int, Int)]], ctx: PatternProcessFunction.Context, out: Collector[String]): Unit = {
        
        events.get("evento2").forEach( value => { println("Evento2: "+value._1); out.collect("Placa clonada: "+value._1) })
        
    }
}
