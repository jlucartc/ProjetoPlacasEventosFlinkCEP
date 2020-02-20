import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

class PlacasPunctualTimestampAssigner extends AssignerWithPunctuatedWatermarks[(String,Double,Double,String,Int,Int)]{
    
    var counter : Int = 0
    var eventsUntilNextWatermark : Int = 1
    var lastTimestamp : Long = _
    
    override def checkAndGetNextWatermark(lastElement: (String, Double, Double, String, Int, Int), extractedTimestamp: Long): Watermark = {
    
        if(counter == eventsUntilNextWatermark){
            
            counter = 0
            
            var time = new Timestamp(lastTimestamp)
            println("Watermark: ",time.toString)
            new Watermark(lastTimestamp)
            
        }else{
            
            null
        
        }
    
    }
    
    override def extractTimestamp(element: (String,Double, Double,String,Int,Int), previousElementTimestamp: Long): Long = {
    
    
        val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = formatter.parse(element._4)
        val timestampAtual = new Timestamp(date.getTime).getTime
        lastTimestamp = Math.max(lastTimestamp,timestampAtual)
        
        counter = counter + 1
        
        timestampAtual
    
    }
}
