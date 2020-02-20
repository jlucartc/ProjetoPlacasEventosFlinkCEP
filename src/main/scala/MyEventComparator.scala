import org.apache.flink.cep.EventComparator

class MyEventComparator extends EventComparator[(String,Double,Double,String,Int,Int)]{
    
    override def compare(t: (String, Double, Double, String, Int, Int), t1: (String, Double, Double, String, Int, Int)): Int = {
        
            if(t._4 > t1._4){
                
                1
                
            }else if( t._4 == t1._4){
                
                0
                
            }else{
                
                -1
                
            }
    
    }

}
