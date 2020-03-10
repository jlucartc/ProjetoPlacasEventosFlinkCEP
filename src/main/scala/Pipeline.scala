import java.util.Properties
import java.util.concurrent.TimeUnit

import Functions.S2TMapFunction
import IterativeConditions.Evento1ConditionFunction
import KeySelectors.{EventKeySelector, TupleKeySelector}
import PatternProcessFunctions.Evento1PatternProcessFunction
import ProcessFunctions.FollowDetectorProcessFunction
import TimestampAssigners.PlacasPunctualTimestampAssigner
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.time.Time
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
    val timeLimitSeconds = 120
    val seconds = 421
    
    var env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    env.enableCheckpointing(1000)
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(1,org.apache.flink.api.common.time.Time.of(10, TimeUnit.SECONDS)))
    var props : Properties = new Properties()
    
    props.setProperty("bootstrap.servers","localhost:32768")
    props.setProperty("zookeeper.connect","localhost:2181")
    props.setProperty("group.id","flink-consumer")
    props.setProperty("auto.offset.reset", "earliest")
    props.setProperty("enable.auto.commit","false")
    
    var stream : DataStream[String] = env.readTextFile("/home/luca/Desktop/input").uid("TextFileInput").name("Stream original")
    
    //var stream : DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("placas",new SimpleStringSchema(),props)).uid("KafkaConsumerInput")
    
    var tupleStream = stream
        .map(new S2TMapFunction()).uid("S2TMapFunction").name("S2TMapFunction")
        .assignTimestampsAndWatermarks(new PlacasPunctualTimestampAssigner()).uid("PlacasPunctualTimestampAssigner")
        .keyBy(new TupleKeySelector())
    
    var newTupleStream = tupleStream.process(new FollowDetectorProcessFunction(timeLimitSeconds)).uid("FollowDetectorProcessFunction").name("newTupleStream")
        .keyBy(new EventKeySelector())
    
    val pattern = Pattern
        .begin[(Int,Int,String,String,Long)]("evento1")
        .where(new Evento1ConditionFunction())
        .times(2)
        .within(org.apache.flink.streaming.api.windowing.time.Time.seconds(seconds))
    
    //val pattern = Pattern.begin[(String,Double,Double,String,Int,Int)]("evento2").where(new IterativeConditions.Evento2ConditionFunction(maxSpeed))
    //val pattern = Pattern.begin[(String,Double,Double,String,Int,Int)]("evento3").where(new IterativeConditions.Evento3ConditionFunction(qChange))
    
    val patternStream = CEP.pattern(newTupleStream,pattern)
    
    val result = patternStream.process(new Evento1PatternProcessFunction()).uid("Evento1PatternProcessFunction").name("Evento 1")
    //val result = patternStream.process(new PatternProcessFunctions.Evento2PatternProcessFunction())
    //val result = patternStream.process(new PatternProcessFunctions.Evento3PatternProcessFunction())
    
    stream.writeAsText("/home/luca/Desktop/output2",FileSystem.WriteMode.OVERWRITE).name("Storage")
    //tupleStream.writeAsText("/home/luca/Desktop/input",FileSystem.WriteMode.OVERWRITE)
    result.writeAsText("/home/luca/Desktop/output",FileSystem.WriteMode.OVERWRITE).uid("WriteResultToTextFile").name("OutputFile")
    
    println("Execution plan: \n\n"+env.getExecutionPlan)
    
    env.execute()
    
    
    
}
