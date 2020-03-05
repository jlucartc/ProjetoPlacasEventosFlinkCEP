import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.streaming.api.functions.{KeyedProcessFunction, ProcessFunction}
import org.apache.flink.util.Collector

class FollowDetectorProcessFunction(timeLimitSeconds : Double) extends ProcessFunction[(String,Double,Double,Long,Int,Int),(Int,Int,String,String,Long)]{
    
    private var map : MapState[String,Array[(String,Long)]] = _
    
    override def processElement(value: (String, Double, Double, Long, Int, Int), ctx: ProcessFunction[(String, Double, Double, Long, Int, Int), (Int, Int, String, String, Long)]#Context, out: Collector[(Int, Int, String, String, Long)]): Unit = {
        
        if(map == null){
            
            map = getRuntimeContext.getMapState(new MapStateDescriptor[String,Array[(String,Long)]]("FollowDetectorState",classOf[String],classOf[Array[(String,Long)]]))
            
            map.put(value._5.toString+"."+value._6.toString,Array((value._1,value._4)))
            
        }else{
            
            val output = updateMap(value)
            
            output.foreach( el => {
                
                println("Output: ",el.toString())
                out.collect(el)
                
            })
            
        }
        
    }
    
    
    def updateMap(value : (String, Double, Double, Long, Int, Int)): Array[(Int,Int,String,String,Long)] = {
        
        var list = map.get(value._5+"."+value._6)
    
        var output: Array[(Int, Int, String, String, Long)] = Array()
        
        if(list != null && list.length != 0) {
            
            println("List("+value._5+"."+value._6+"): "+list.mkString(","))
            
            list = list.filter( el => {
        
                if (el._1 != value._1) {
            
                    if (value._4 - el._2 <= timeLimitSeconds * 1000) {
                
                        output = output ++ Array((value._5, value._6, el._1, value._1, value._4))
                
                        true
                
                    } else {
                
                        false
                
                    }
            
                } else {
            
                    false
            
                }
        
            })
    
            list = list ++ Array((value._1, value._4))
    
            map.put(value._5 + "." + value._6, list)
    
            output
            
        }else {
    
            map.put(value._5 + "." + value._6,Array((value._1,value._4)))
            
            output
    
        }
        
    }
    
    
}
