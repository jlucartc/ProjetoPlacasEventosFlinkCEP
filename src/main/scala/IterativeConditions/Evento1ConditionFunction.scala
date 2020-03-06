package IterativeConditions

import java.sql.Timestamp

import org.apache.flink.cep.pattern.conditions.IterativeCondition

class Evento1ConditionFunction extends IterativeCondition[(Int,Int,String,String,Long)]{

    override def filter(value: (Int,Int,String,String,Long), ctx: IterativeCondition.Context[(Int,Int,String,String,Long)]): Boolean = {

        true
        
    }
}
