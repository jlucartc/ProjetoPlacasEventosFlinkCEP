import org.apache.flink.cep.pattern.conditions.SimpleCondition

class MySimpleCondition() extends SimpleCondition[String]{
    override def filter(value: String): Boolean = {
        
        if(value == "Hello"){
            true
        }else{
            false
        }
        
    }
}
