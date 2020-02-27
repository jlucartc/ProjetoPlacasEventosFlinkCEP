import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util
import org.apache.flink.cep.pattern.conditions.IterativeCondition
import scala.collection.mutable

class Evento1ConditionFunction(val intervaloTsegundos : Int, val q : Int) extends IterativeCondition[(String,Double,Double,String,Int,Int)]{
    
    private val map : mutable.HashMap[Int,Array[String]] = new mutable.HashMap[Int,Array[String]]()
    private var counter = 1
    
    override def filter(value: (String,Double,Double,String,Int,Int), ctx: IterativeCondition.Context[(String,Double,Double,String,Int,Int)]): Boolean = {
        
        println("SameRegionFunction: "+value._1)
        this.counter = this.counter + 1
        
        map.foreach( el => {
            
            val newEl = el._2.filter( str => {
    
                val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = formatter.parse(str)
                val timestampAtual = new Timestamp(date.getTime).getTime
                val date2 = formatter.parse(el._2(el._2.length-1))
                val maiorTimestamp = new Timestamp(date2.getTime).getTime
                
                if(timestampAtual + (intervaloTsegundos*1000) < maiorTimestamp){
                
                    false
                
                }else{

                    true
                    
                }
            
            })
            
            this.map.put(el._1,newEl) match {
    
                case Some(res) => {}

                case None => {}
                
            }
            
            
            
        })
        
        val returnVal : Boolean = this.map.get(value._6) match {
    
            case Some(res) => {
                    
                    if(res.length+1 >= q){
                    
                        val newArr = (res ++ Array(value._4)).slice(1,res.length+1)
                        
                        this.map.put(value._6,newArr) match {
    
                            case Some(res) => {
                                
                                true
                                
                            }
                            
                            case None => {
                                
                                false
                                
                            }
                        
                        }
                        
                        
                    }else {
                    
                        val newRes = res ++ Array(value._4)
                        
                        this.map.put(value._6,newRes) match {
    
                            case Some(res) => {
    
                                false
                                
                            }

                            case None => {
                                
                                false
                                
                            }
                        }
                        
                        false
                        
                    }
                
            }
        
            case None => {
                
                this.map.put(value._6,Array(value._4)) match {
    
                    case Some(res) => {
                        
                        false
                        
                    }

                    case None => {
                        
                        false
                        
                    }
                    
                }
            
            }
            
        }
        
        returnVal
        
    }
}
