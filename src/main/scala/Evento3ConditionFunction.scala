import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.apache.flink.cep.pattern.conditions.IterativeCondition

import scala.collection.mutable

class Evento3ConditionFunction(qChange: Int) extends IterativeCondition[(String,Double,Double,String,Int,Int)] {
    
    private val map : mutable.HashMap[String,(Int,Double,String,Long)] = new mutable.HashMap[String,(Int,Double,String,Long)]()
    
    override def filter(value: (String, Double, Double, String, Int, Int), ctx: IterativeCondition.Context[(String, Double, Double, String, Int, Int)]): Boolean = {
    
        this.map.get(value._5.toString+"."+value._6.toString) match {
    
            case Some(res) => {
                
                val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = formatter.parse(value._4)
                val timestampAtual = new Timestamp(date.getTime).getTime
                
                val q = res._1
                val media = res._2
                val lastTimestamp = new Timestamp(formatter.parse(res._3).getTime).getTime
                val lastTotalTime = res._4
                val novaMedia = (q+1)/((lastTotalTime + (timestampAtual - lastTimestamp))/60000.0)
                
                println("lastTotalTime: "+lastTotalTime.toString)
                println("lastTimestamp: "+lastTimestamp.toString)
                println("media: "+media.toString)
                println("novaMedia: "+novaMedia.toString)
                println("Change: "+((Math.abs(novaMedia - media)/media)*100.0).toString+"%")
                println("\n\n")
                
                if((Math.abs(novaMedia - media)/media)*100.0 > qChange){
                    
                    this.map.put(value._5.toString+"."+value._6.toString,(q+1,novaMedia,value._4,lastTotalTime+(timestampAtual-lastTimestamp))) match {
    
                        case Some(res) => {
                            
                            true
                            
                        }
                        
                        case None => {
                            
                            true
                            
                        }
                    }
                    
                }else{
    
                    this.map.put(value._5.toString+"."+value._6.toString,(q+1,novaMedia,value._4,lastTotalTime+(timestampAtual-lastTimestamp))) match {
        
                        case Some(res) => {
            
                            false
            
                        }
                        case None => {
            
                            false
            
                        }
                    }
                    
                }
                
            }

            case None => {
                
                this.map.put(value._5+"."+value._6,(1,1,value._4,0)) match {
    
                    case Some(res) => { false}

                    case None => { false }
                    
                }
                
            }
            
        }
        
    }
    
}
