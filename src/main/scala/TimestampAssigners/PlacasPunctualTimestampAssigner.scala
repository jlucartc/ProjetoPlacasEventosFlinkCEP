package TimestampAssigners

import java.sql.Timestamp

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

class PlacasPunctualTimestampAssigner extends AssignerWithPunctuatedWatermarks[(String,Double,Double,Long,Int,Int)]{
    
    var counter : Int = 0
    var eventsUntilNextWatermark : Int = 0
    var lastTimestamp : Long = _
    
    override def checkAndGetNextWatermark(lastElement: (String, Double, Double, Long, Int, Int), extractedTimestamp: Long): Watermark = {
    
        if(counter == eventsUntilNextWatermark){
            
            counter = 0
            
            var time = new Timestamp(lastTimestamp)
            println("Watermark: ",time.toString)
            new Watermark(lastTimestamp)
            
        }else{
            
            null
        
        }
    
    }
    
    override def extractTimestamp(element: (String,Double, Double,Long,Int,Int), previousElementTimestamp: Long): Long = {
    
        
        lastTimestamp = Math.max(lastTimestamp,element._4)
        
        //counter = counter + 1
        
        element._4
    
    }
}
