import java.sql.Timestamp
import java.util.Properties

import org.apache.flink.api.scala._
import org.apache.commons.lang3.RandomStringUtils
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

import scala.util.Random

class ProdutorPlacas extends Thread {

    override def run(): Unit ={
        
        publicarPlacas(10000)
        
    }
    
    def publicarPlacas(quantidade : Int): Unit = {
        
        var counter = 0;
        
        while(counter < quantidade) {
    
            val props = new Properties()
            props.put("bootstrap.servers", "localhost:32768")
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            props.put("acks", "1")
    
            var placa = RandomStringUtils.randomAlphabetic(8)
    
            val rangebegin = Timestamp.valueOf("2013-02-08 00:00:00").getTime
            //val rangeend = Timestamp.valueOf("2013-02-08 00:58:00").getTime
            //val diff = rangeend - rangebegin + 1
            //val timestamp = new Timestamp(rangebegin + (Math.random * diff).toLong)
            val timestamp = new Timestamp(rangebegin + 10000*counter)
            
            val rand = new Random()
            val posLatMin = 0.00001
            val posLatMax = 0.05
            val posLngMin = 0.0001
            val posLngMax = 0.01
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
