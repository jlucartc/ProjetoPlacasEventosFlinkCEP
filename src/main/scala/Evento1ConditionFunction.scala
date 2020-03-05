import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util
import org.apache.flink.cep.pattern.conditions.IterativeCondition
import scala.collection.mutable

class Evento1ConditionFunction extends IterativeCondition[(Int,Int,String,String,Long)]{

    override def filter(value: (Int,Int,String,String,Long), ctx: IterativeCondition.Context[(Int,Int,String,String,Long)]): Boolean = {

        true
        
    }
}
