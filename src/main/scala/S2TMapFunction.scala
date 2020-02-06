import java.sql.Timestamp

import org.apache.flink.api.common.functions.MapFunction


class S2TMapFunction0() extends MapFunction[String,(String,String,Double,Double)]{
    
    override def map(value: String): (String,String,Double,Double) = {
    
        val tuple = value.substring(1,value.length-1).split(',')
        val coords = tuple(2).substring(1,tuple(2).length-1).split(',')
        
        
        (tuple(0),tuple(1),coords(0).toDouble,coords(1).toDouble)
        
        
        
    }
}
