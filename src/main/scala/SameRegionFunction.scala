import org.apache.flink.cep.pattern.conditions.IterativeCondition

import scala.collection.mutable

class SameRegionFunction() extends IterativeCondition[(String,Double,Double,String,Int,Int)]{
    
    private val map : mutable.HashMap[Int, Int] = new mutable.HashMap[Int,Int]()
    private var counter = 1
    
    override def filter(value: (String,Double,Double,String,Int,Int), ctx: IterativeCondition.Context[(String,Double,Double,String,Int,Int)]): Boolean = {
        
        //println("SameRegionFunction: "+this.counter.toString)
        println("SameRegionFunction: "+value._1)
        this.counter = this.counter + 1
        
        val returnVal : Boolean = this.map.get(value._6) match {
    
            case Some(res) => {
    
                //println("Counter: "+res.toString)
                
                if(res+1 >= 5){
                    
                    this.map.remove(value._6) match {
    
                        case default => {
                            
                            //println("True");
                            
                            true
                        }
                        
                    }
                    
                    
                }else{
                    
                    
                    this.map.put(value._6,res+1) match {
    
                        case default => {
                            
                            //println("False");
                            
                            false

                        }
                        
                    }
                    
                }
                
            }
            case None => {
    
                //println("Counter: 0")
                
                this.map.put(value._6,1) match {
    
                    case default => {
                        
                        //println("False")
                        
                        false
                        
                    }
    
    
                }
            }
            
        }
        
        returnVal
        
    }
}
