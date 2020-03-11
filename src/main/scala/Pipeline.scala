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
    val parallelism = 1
    val checkPointingTimeInterval = 1000
    val restartAttempts = 1
    val timeBeforeRetry = 10000
    val inputFileURL1 = "/home/luca/Desktop/input"
    val outputFileURL1 = "/home/luca/Desktop/output"
    val outputFileURL2 = "/home/luca/Desktop/output2"
    val kafkaConsumerTopicName = "placas"
    
    var env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(parallelism)
    env.enableCheckpointing(checkPointingTimeInterval)
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(restartAttempts,org.apache.flink.api.common.time.Time.of(timeBeforeRetry, TimeUnit.MILLISECONDS)))
    var props : Properties = new Properties()
    
    props.setProperty("bootstrap.servers","localhost:32768")
    props.setProperty("zookeeper.connect","localhost:2181")
    props.setProperty("group.id","flink-consumer")
    props.setProperty("auto.offset.reset", "earliest")
    props.setProperty("enable.auto.commit","false")
    
    var stream : DataStream[String] = env.readTextFile(inputFileURL1).uid("TextFileInput").name("Stream original")
    
    //var stream : DataStream[String] = env.addSource(new FlinkKafkaConsumer[String](kafkaConsumerTopicName,new SimpleStringSchema(),props)).uid("KafkaConsumerInput")
    
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
    
    stream.writeAsText(outputFileURL2,FileSystem.WriteMode.OVERWRITE).name("Storage")
    result.writeAsText(outputFileURL1,FileSystem.WriteMode.OVERWRITE).uid("WriteResultToTextFile").name("OutputFile")
    
    println("Execution plan: \n\n"+env.getExecutionPlan)
    
    env.execute()
    
    
    
}
