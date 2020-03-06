package Functions

import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.util.URLParser
import com.github.mauricio.async.db.{Connection, QueryResult, RowData}
import org.apache.flink.runtime.concurrent.Executors
import org.apache.flink.streaming.api.scala.async.{AsyncFunction, ResultFuture}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

class AsyncPostgresFunction extends AsyncFunction[(String,String,Double,Double),String] {
    
    implicit lazy val executor: ExecutionContext = ExecutionContext.fromExecutor(Executors.directExecutor())
    
    override def asyncInvoke(input: (String,String,Double,Double), resultFuture: ResultFuture[String]): Unit = {
    
        val configuration = URLParser.parse("jdbc:postgresql://localhost:5432/mydb?user=luca&password=root")
        val connection: Connection = new PostgreSQLConnection(configuration)
        
        val duration : DurationInt = new DurationInt(1000)
        
        Await.result(connection.connect,duration.milliseconds)
        
        val future : Future[QueryResult] = connection.sendQuery("SELECT * FROM areas")
        
        //val future  : Future[QueryResult] = connection.sendQuery("SELECT * FROM areas WHERE ( tipo = 'poligono' AND ST_ContainsProperly(poligono::geometry,ST_GeographyFromText('point("+input._3.toString+" "+input._4.toString+")')::geometry)) OR ( tipo = 'circulo' AND ST_Distance(centro,ST_GeographyFromText('point("+input._3.toString+" "+input._4.toString+")')) <= raio )")
        
        val mapResult: Future[Any] = future.map(queryResult => queryResult.rows match {
            case Some(resultSet) => {
                val row : RowData = resultSet.head
                
                println("\n\nresultSetLength: "+resultSet.length.toString+"\n\n")
                
                if(resultSet.length > 0){
                
                    resultFuture.complete(Iterable("Evento: ("+input._3.toString+","+input._4.toString+")"))
                    
                }else{
                
                    resultFuture.complete(Iterable("Não é evento: ("+input._3.toString+","+input._4.toString+")"))
                    
                }
                
                connection.disconnect
                row
            }
            
            case None => {resultFuture.complete(Iterable("NULL")) ; println("\n\nNULL\n\n"); connection.disconnect}

            case default => { connection.disconnect }
        }
        )
        
        
    }

}
