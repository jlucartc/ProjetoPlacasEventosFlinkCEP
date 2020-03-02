import org.apache.flink.cep.pattern.conditions.IterativeCondition

class EventoTesteConditionFunction(var counter : Int) extends IterativeCondition[(String,Double,Double,String,Int,Int)] {
    
    private var c  : Int = 0
    
    override def filter(value: (String, Double, Double, String, Int, Int), ctx: IterativeCondition.Context[(String, Double, Double, String, Int, Int)]): Boolean = {
        
        if(c == counter-1){
    
            println("Event: "+value._1.toString+","+value._6.toString)
            
            c = 0
            
            true
            
        }else{
            
            c = c + 1

            false
            
        }
        
    }
}
