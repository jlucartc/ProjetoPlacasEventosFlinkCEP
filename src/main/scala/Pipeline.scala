import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.{TimeCharacteristic, scala}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.scala._
import org.apache.flink.cep.CEP
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

class Pipeline {
    
    var env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    env.setParallelism(1)
    env.getConfig.setAutoWatermarkInterval(2000)
    
    var props : Properties = new Properties()
    
    props.setProperty("bootstrap.servers","localhost:32768")
    props.setProperty("zookeeper.connect","localhost:2181")
    props.setProperty("group.id","flink-consumer")
    props.setProperty("auto.offset.reset", "earliest")
    props.setProperty("enable.auto.commit","false")
    
    //var stream : DataStream[String] = env.readTextFile("/home/luca/Desktop/input").name("Stream original")
    var stream : DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("placas",new SimpleStringSchema(),props))
    
    var tupleStream = stream.map(new S2TMapFunction())
    
    val pattern = Pattern.begin[(String,Double,Double,String,Int,Int)]("all").where(new AcceptAllFunction()).followedBy("follow").where(new SameRegionFunction()).within(Time.minutes(1))
    
    val patternStream = CEP.pattern(tupleStream,pattern)
    
    val result = patternStream.process(new MyPatternProcessFunction())
    
    stream.writeAsText("/home/luca/Desktop/input",FileSystem.WriteMode.OVERWRITE)
    result.writeAsText("/home/luca/Desktop/output",FileSystem.WriteMode.OVERWRITE)
    
    env.execute()
    
}
