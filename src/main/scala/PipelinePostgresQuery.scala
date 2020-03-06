import java.sql.Timestamp
import java.util.Properties
import java.util.concurrent.TimeUnit

import com.github.mauricio.async.db.{QueryResult, ResultSet}
import org.apache.flink.api.scala._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.typeinfo.{TypeInformation, Types}
import org.apache.flink.api.java.io.jdbc.{JDBCAppendTableSink, JDBCOptions, JDBCTableSource}
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{AsyncDataStream, DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.table.api.{DataTypes, EnvironmentSettings, TableSchema}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.types.Row
import org.postgresql.Driver

class PipelinePostgresQuery() {

    /* 1.0 : Testando JDBCTableSource */

        //val jdbcEnv = StreamExecutionEnvironment.getExecutionEnvironment
        //jdbcEnv.setParallelism(1)

        //val settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
        //val tableEnv = StreamTableEnvironment.create(jdbcEnv,settings)

        //val jdbcOptions = JDBCOptions.builder()
        //  .setDriverName("org.postgresql.Driver")
        //  .setDBUrl("jdbc:postgresql://localhost:5432/mydb")
        //  .setUsername("luca")
        //  .setPassword("root")
        //  .setTableName("example")
        //  .build()

        //val jdbcSchema = TableSchema.builder()
        //  .field("name",DataTypes.STRING())
        //  .build()

        //val jdbcsource = JDBCTableSource.builder().setOptions(jdbcOptions).setSchema(jdbcSchema).build()

        //tableEnv.registerTableSource("jdbc_table_source",jdbcsource)

        //val outputDataStream = tableEnv.toAppendStream[Row](tableEnv.sqlQuery("SELECT * FROM jdbc_table_source"))

        //outputDataStream.writeAsText("/home/luca/Desktop/jdbcoutput",FileSystem.WriteMode.OVERWRITE)

        //jdbcEnv.execute()

    /* --- 1.0 FIM ---*/

    /* 2.0 : Testando conexão com BD através de Async I/O  - Funcionou */
    
        //var asyncEnv = StreamExecutionEnvironment.getExecutionEnvironment
        
        //asyncEnv.setParallelism(1)
    
        //var asyncStream : DataStream[String] = asyncEnv.readTextFile("/home/luca/Desktop/inputData").name("Async stream")
    
        //var afterAsyncFunctionStream : DataStream[String] = AsyncDataStream.unorderedWait(asyncStream,new Functions.AsyncPostgresFunction(), 500, TimeUnit.MILLISECONDS, 100)
    
        //afterAsyncFunctionStream.writeAsText("/home/luca/Desktop/asyncIOoutput",FileSystem.WriteMode.OVERWRITE)
    
        //asyncEnv.execute()
    
    /* FIM */
  /*
    var env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)
    env.getConfig.setAutoWatermarkInterval(2000)
    
    var props : Properties = new Properties()
    
    props.setProperty("bootstrap.servers","localhost:32768")
    props.setProperty("zookeeper.connect","localhost:2181")
    props.setProperty("group.id","flink-consumer")
    props.setProperty("auto.offset.reset", "earliest")
    props.setProperty("enable.auto.commit","false")
    
    
    //var stream : DataStream[String] = env.readTextFile("/home/luca/Desktop/inputData").name("Stream original")
    var stream : DataStream[String] = env.addSource(new FlinkKafkaConsumer[String]("placas",new SimpleStringSchema(),props))
    var tupleStream  : DataStream[(String,String,Double,Double)] = stream.map(new Functions.S2TMapFunction())
    
    var earlyDataStream = tupleStream.assignTimestampsAndWatermarks(new TimestampAssigners.PlacasPeriodicTimestampAssigner())
    .process(new ProcessFunctions.RemoveLateDataProcessFunction())
    
    /* A partir daqui, haverá um pipeline para cada tipo de evento */
    
    var afterAsyncFunctionStream : DataStream[String] = AsyncDataStream.unorderedWait(earlyDataStream,new Functions.AsyncPostgresFunction(), 10000, TimeUnit.MILLISECONDS, 500)
    
    //stream.writeAsText("/home/luca/Desktop/asyncIOoutput",FileSystem.WriteMode.OVERWRITE)
    
    afterAsyncFunctionStream.writeAsText("/home/luca/Desktop/asyncIOoutput",FileSystem.WriteMode.OVERWRITE)
    */
    /*  Vou aplicar um map/reduce nos dados do stream.
    *   Primeiro eu checo na função se o dado foi emitido em menos de 10s de acordo com o tempo atual.
    *   Caso o dado obedeça a esse critério, então eu o substituo por um evento que irá para um tópico Kafka,
    *   para que seja lido pela aplicação.
    * */
    
    /*
    Agora que a DataStream original passou pelo filtro de atraso, ela deve ser transformada utilizando a Table API, para
    que consultas ao banco de dados possam ser feitas.
     */
    
    /* Checar o JDBCInputFormat. Provavelmente será mais fácil implementar fazendo a query diretamente da source. Testar
    *  se o JDBCInputFormat permite a entrada de dados novos inseridos após o início da execução da pipeline (Push Notifications).
    * */


/*    val res = env.execute()
    println("Execution time: ",res.getNetRuntime(TimeUnit.MILLISECONDS))*/
    
}
