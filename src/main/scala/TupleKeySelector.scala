import org.apache.flink.api.java.functions.KeySelector

class TupleKeySelector() extends KeySelector[(String,Double,Double,String,Int,Int),Int]{
    
    override def getKey(value: (String, Double, Double, String, Int, Int)): Int = {
    
        value._6
        
    }
}
