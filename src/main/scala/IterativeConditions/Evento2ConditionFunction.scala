package IterativeConditions

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.apache.flink.cep.pattern.conditions.IterativeCondition

import scala.collection.mutable

case class Evento2ConditionFunction(val maxSpeed : Float) extends IterativeCondition[(String,Double,Double,String,Int,Int)]{
    
    private val map : mutable.HashMap[String,(Double,Double,String)] = new mutable.HashMap[String,(Double,Double,String)]()
    
    override def filter(value: (String, Double, Double, String, Int, Int), ctx: IterativeCondition.Context[(String, Double, Double, String, Int, Int)]): Boolean = {
    
        this.map.get(value._1) match {
    
            case Some(res) => {
    
                val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = formatter.parse(value._4)
                val timestampAtual = new Timestamp(date.getTime).getTime
                val date2 = formatter.parse(res._3)
                val timestampAntigo = new Timestamp(date2.getTime).getTime
                
                if(((distance(res._1,value._2,res._2,value._3,0,0))/1000)/((timestampAtual - timestampAntigo)/3600000) > maxSpeed){
                    
                    println("Velocidade suspeita(Km/h): "+(((distance(res._1,value._2,res._2,value._3,0,0))/1000.0)/((timestampAtual - timestampAntigo)/3600000.0)).toString)
                    
                    this.map.put(value._1,(value._2,value._3,value._4)) match {
    
                        case Some(res) => { true }
                        case None => { true }
                    
                    }
                    
                    
                }else{
                    
                    this.map.put(value._1,(value._2,value._3,value._4)) match {
    
                        case Some(res) => { false }
                        case None => { false }
                        
                    }
                    
                }
                
                
            }
            
            case None => {
                
                this.map.put(value._1,(value._2,value._3,value._4)) match {
    
                    case Some(res) => { false }

                    case None => { false }
                    
                }
                
            }
            
        }
    
    }
    
    def distance(lat1: Double, lat2: Double, lon1: Double, lon2: Double, el1: Double, el2: Double): Double = {
        val R = 6371 // Radius of the earth
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R * c * 1000 // convert to meters
        val height = el1 - el2
        distance = Math.pow(distance, 2) + Math.pow(height, 2)
        Math.sqrt(distance)
    }
}
