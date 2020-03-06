package Functions

import org.apache.flink.api.common.functions.MapFunction

class DoNothingMapFunction() extends MapFunction[(String,Double,Double,String,Int,Int),(String,Double,Double,String,Int,Int)]{
    override def map(value: (String, Double, Double, String, Int, Int)): (String, Double, Double, String, Int, Int) = {
        
        value
        
    }
}
