import com.github.mauricio.async.db.{Connection, QueryResult, ResultSet, RowData}
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.util.URLParser
import org.apache.flink.streaming.api.scala.async.{AsyncFunction, ResultFuture}
import org.apache.flink.api.scala._
import org.apache.flink.runtime.concurrent.Executors

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

class AsyncPostgresFunction extends AsyncFunction[(String,String,Double,Double),String] {
    
    implicit lazy val executor: ExecutionContext = ExecutionContext.fromExecutor(Executors.directExecutor())
    
    override def asyncInvoke(input: (String,String,Double,Double), resultFuture: ResultFuture[String]): Unit = {
    
        val configuration = URLParser.parse("jdbc:postgresql://localhost:5432/mydb?user=luca&password=root")
        val connection: Connection = new PostgreSQLConnection(configuration)
        
        val duration : DurationInt = new DurationInt(1000)
        
        Await.result(connection.connect,duration.milliseconds)
        
        val future  : Future[QueryResult] = connection.sendQuery("SELECT * FROM areas WHERE poligono @> point("+input._3+","+input._4+") OR circulo @> point("+input._3+","+input._4+")")
        
        val mapResult: Future[Any] = future.map(queryResult => queryResult.rows match {
            case Some(resultSet) => {
                val row : RowData = resultSet.head
                
                if(resultSet.length > 0){
                
                    resultFuture.complete(Iterable("Evento"))
                    
                }else{
                
                    resultFuture.complete(Iterable("Não é evento"))
                    
                }
                
                connection.disconnect
                row
            }
            case None => -1
        }
        )
        
        
    }

}
