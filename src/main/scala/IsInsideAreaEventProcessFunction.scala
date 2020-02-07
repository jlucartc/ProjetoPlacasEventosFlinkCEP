import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

class IsInsideAreaEventProcessFunction extends ProcessFunction[(String,String,Double,Double),(String,String,Double,Double)] {
    
    override def processElement(value: (String, String, Double, Double), ctx: ProcessFunction[(String, String, Double, Double), (String, String, Double, Double)]#Context, out: Collector[(String, String, Double, Double)]): Unit = {
    
    
    
    }
}
