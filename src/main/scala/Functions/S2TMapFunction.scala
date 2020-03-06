package Functions

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.apache.flink.api.common.functions.MapFunction


class S2TMapFunction extends MapFunction[String,(String,Double,Double,Long,Int,Int)]{
    
    override def map(value: String): (String,Double,Double,Long,Int,Int) = {
    
        //val tuple = value.split(',')
        //val coords = Array(tuple(1),tuple(2))
        
        //(tuple(0),coords(0).toDouble,coords(1).toDouble,tuple(3),tuple(4).toInt,tuple(5).toInt)
    
        println("String: "+value)
        
        val tuple = value.substring(0,value.length-1).split(',')
        
        val coords = Array(tuple(1),tuple(2))
    
        val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = formatter.parse(tuple(3))
        val timestampAtual = new Timestamp(date.getTime).getTime
    
        (tuple(0),coords(0).toDouble,coords(1).toDouble,timestampAtual,tuple(4).toInt,tuple(5).toInt)
        
    }
}
