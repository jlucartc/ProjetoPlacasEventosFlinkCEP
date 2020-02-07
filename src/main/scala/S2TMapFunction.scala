import java.sql.Timestamp

import org.apache.flink.api.common.functions.MapFunction


class S2TMapFunction extends MapFunction[String,(String,String,Double,Double)]{
    
    override def map(value: String): (String,String,Double,Double) = {
    
        val tuple = value.substring(1,value.length-1).split(',')
        val coords = Array(tuple(2).replaceAllLiterally("(",""),tuple(3).replaceAllLiterally(")",""))
        
        (tuple(0),tuple(1),coords(0).toDouble,coords(1).toDouble)
        
        
        
    }
}
