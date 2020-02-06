import java.sql.Timestamp
import java.util.Properties

import org.apache.flink.api.scala._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer


class Pipeline() {

    var env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    
    var props : Properties = new Properties()
    
    props.setProperty("bootstrap.servers","localhost:32768")
    props.setProperty("zookeeper.connect","localhost:2181")
    props.setProperty("group.id","flink-consumer")
    
    var stream : DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("placas",new SimpleStringSchema(),props))
    var tupleStream  : DataStream[(String,String,Double,Double)] = stream.map(new S2TMapFunction())
    
    /*  Vou aplicar um map/reduce nos dados do stream.
    *   Primeiro eu checo na função se o dado foi emitido em menos de 10s de acordo com o tempo atual.
    *   Caso o dado obedeça a esse critério, então eu o substituo por um evento que irá para um tópico Kafka,
    *   para que seja lido pela aplicação.
    * */
    
    stream.writeAsText("/home/luca/Desktop/output",FileSystem.WriteMode.OVERWRITE)
    
    env.execute()

    
    
}
