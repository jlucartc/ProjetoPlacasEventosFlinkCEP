import java.sql.Timestamp

import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

class RemoveLateDataProcessFunction extends ProcessFunction[(String,Double, Double,String,Int,Int),(String,Double, Double,String,Int,Int)]{
    
    override def processElement(value: (String,Double, Double,String,Int,Int), ctx: ProcessFunction[(String,Double, Double,String,Int,Int), (String,Double, Double,String,Int,Int)]#Context, out: Collector[(String,Double, Double,String,Int,Int)]): Unit = {
    
        val watermark = ctx.timerService().currentWatermark()
        
        val t = new Timestamp(ctx.timestamp())
        
        if(ctx.timestamp() < watermark){
            
        }else if(ctx.timestamp() >= watermark){
            
            out.collect(value)
            
        }
    
    }

}
