package IterativeConditions

import org.apache.flink.cep.pattern.conditions.IterativeCondition

class EventoTesteIterativeCondition() extends IterativeCondition[(String,Double,Double,String,Int,Int)] {
    
    
    def filter(value: (String, Double, Double, String, Int, Int), ctx: IterativeCondition.Context[(String, Double, Double, String, Int, Int)]): Boolean = {

        println("Id: "+value._6.toString)
        
        true
        
    }

}
