import java.sql.Timestamp
import java.util.Properties

import org.apache.flink.api.scala._
import org.apache.commons.lang3.RandomStringUtils
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

import scala.util.Random

class ProdutorPlacas extends Thread {

    override def run(): Unit ={
        
        publicarPlacas(5000)
        
    }
    
    def publicarPlacas(quantidade : Int): Unit = {
        
        var counter = 0;
        
        while(counter < quantidade) {
    
            val props = new Properties()
            props.put("bootstrap.servers", "localhost:32768")
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            props.put("acks", "1")
    
            var placa = RandomStringUtils.random(8)
    
            val rangebegin = Timestamp.valueOf("2013-02-08 00:00:00").getTime
            val rangeend = Timestamp.valueOf("2013-02-08 00:58:00").getTime
            val diff = rangeend - rangebegin + 1
            val timestamp = new Timestamp(rangebegin + (Math.random * diff).toLong)
    
            val rand = new Random()
            val posLatMin = -3.78
            val posLatMax = -3.72
            val posLngMin = -38.59
            val posLngMax = -38.49
            val posicao = Array((rand.nextFloat() * (posLatMax - posLatMin)) + posLatMin, (rand.nextFloat() * (posLngMax - posLngMin)) + posLngMin)
            val dados = Array(placa, timestamp, posicao.mkString("(",",",")"))
    
    
            val record: ProducerRecord[String, String] = new ProducerRecord("placas", dados.mkString("(",",",")"))
            val producer: Producer[String, String] = new KafkaProducer(props)
    
            producer.send(record)
            producer.close()
            
            counter = counter + 1
            
        }
        
    }
    

}
