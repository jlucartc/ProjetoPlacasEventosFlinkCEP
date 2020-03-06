package KeySelectors

import org.apache.flink.api.java.functions.KeySelector

class TupleKeySelector() extends KeySelector[(String,Double,Double,Long,Int,Int),Int]{
    
    override def getKey(value: (String, Double, Double,Long, Int, Int)): Int = {
    
        //value._5
        1
        
    }
}
