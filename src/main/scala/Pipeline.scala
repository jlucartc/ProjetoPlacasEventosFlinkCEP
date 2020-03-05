import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.{TimeCharacteristic, scala}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.scala._
import org.apache.flink.cep.CEP
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.api.datastream.{DataStream, KeyedStream}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

class Pipeline {
    
    val intervaloTsegundos = 600
    val qCarros = 5
    val maxSpeed = 250
    val qChange = 25
    
    var env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    
    var props : Properties = new Properties()
    
    props.setProperty("bootstrap.servers","localhost:32768")
    props.setProperty("zookeeper.connect","localhost:2181")
    props.setProperty("group.id","flink-consumer")
    props.setProperty("auto.offset.reset", "earliest")
    props.setProperty("enable.auto.commit","false")
    
    var stream : DataStream[String] = env.readTextFile("/home/luca/Desktop/input").name("Stream original")
    //var stream : DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("placas",new SimpleStringSchema(),props))
    
    var tupleStream = stream
        .map(new S2TMapFunction())
        .assignTimestampsAndWatermarks(new PlacasPunctualTimestampAssigner())
        .keyBy(new TupleKeySelector())
    
    var newTupleStream = tupleStream.process(new FollowDetectorProcessFunction(120)).keyBy(new EventKeySelector())
    
    val pattern = Pattern.begin[(Int,Int,String,String,Long)]("evento1").where(new Evento1ConditionFunction()).times(2).within(Time.seconds(421))
    //val pattern = Pattern.begin[(String,Double,Double,String,Int,Int)]("evento2").where(new Evento2ConditionFunction(maxSpeed))
    //val pattern = Pattern.begin[(String,Double,Double,String,Int,Int)]("evento3").where(new Evento3ConditionFunction(qChange))
    
    
    val patternStream = CEP.pattern(newTupleStream,pattern)
    
    val result = patternStream.process(new Evento1PatternProcessFunction())
    //val result = patternStream.process(new Evento2PatternProcessFunction())
    //val result = patternStream.process(new Evento3PatternProcessFunction())
    
    //newTupleStream.writeAsText("/home/luca/Desktop/output",FileSystem.WriteMode.OVERWRITE)
    //tupleStream.writeAsText("/home/luca/Desktop/input",FileSystem.WriteMode.OVERWRITE)
    result.writeAsText("/home/luca/Desktop/output",FileSystem.WriteMode.OVERWRITE)
    env.execute()
    
}
