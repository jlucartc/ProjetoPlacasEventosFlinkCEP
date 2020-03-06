package ProcessFunctions

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

class PerKeySumProcessFunction extends ProcessFunction[(String,Double, Double,String,Int,Int),(String,Double, Double,String,Int,Int)]{
    
    private var counter : ValueState[Int] = _
    
    override def processElement(value: (String, Double, Double, String, Int, Int), ctx: ProcessFunction[(String, Double, Double, String, Int, Int), (String, Double, Double, String, Int, Int)]#Context, out: Collector[(String, Double, Double, String, Int, Int)]): Unit = {
        
        if(counter == null){
            counter = getRuntimeContext.getState(new ValueStateDescriptor[Int]("sum", classOf[Int]))
            counter.update(1)
            println("Counter("+value._6+"): 1")
            out.collect(value)
        }else{
    
            var q = counter.value()
            
            q = q+1
            
            counter.update(q)
            
            println("Counter("+value._6+"): "+q.toString)
    
            out.collect(value)
            
        }
        
        
    }
}
