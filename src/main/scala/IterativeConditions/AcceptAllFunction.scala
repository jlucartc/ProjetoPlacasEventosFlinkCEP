package IterativeConditions

import org.apache.flink.cep.pattern.conditions.IterativeCondition

class AcceptAllFunction extends IterativeCondition[(String,Double,Double,String,Int,Int)]{
    
    override def filter(value: (String, Double, Double, String, Int, Int), ctx: IterativeCondition.Context[(String, Double, Double, String, Int, Int)]): Boolean = {

        true
        
    }
    
}
