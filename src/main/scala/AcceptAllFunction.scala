import org.apache.flink.cep.pattern.conditions.IterativeCondition

class AcceptAllFunction extends IterativeCondition[(String,Double,Double,String,Int,Int)]{
    
    private var counter = 1;
    
    override def filter(value: (String, Double, Double, String, Int, Int), ctx: IterativeCondition.Context[(String, Double, Double, String, Int, Int)]): Boolean = {
        //println("AcceptAllFunction: "+this.counter.toString)
        println("AcceptAllFunction: "+value._6)
        this.counter = this.counter + 1
        true
        
    }
    
}
