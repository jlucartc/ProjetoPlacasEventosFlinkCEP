import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.cep.pattern.conditions.{IterativeCondition, RichAndCondition, RichIterativeCondition}

class EventoTesteConditionFunction(var counter : Int) extends RichAndCondition[(String,Double,Double,String,Int,Int)] {
    
    
    
    private var c  : ValueState[Int] = _
    
    override def filter(value: (String, Double, Double, String, Int, Int), ctx: IterativeCondition.Context[(String, Double, Double, String, Int, Int)]): Boolean = super.filter(value, ctx)
    
    override def getLeft: IterativeCondition[(String, Double, Double, String, Int, Int)] = super.getLeft
    
    override def getRight: IterativeCondition[(String, Double, Double, String, Int, Int)] = super.getRight
}
