package initProblem

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise}
import scala.util.Success

object Slide5_0_Scala extends App {

  val customExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  val a: Future[String] = Future(hardWorkFunction("test"))(customExecutionContext)

  Future.successful(hardWorkFunction("test"))

  val logs = List.tabulate(2)( i => s"log$i")

  logs.foreach(log => Future(hardWorkFunction(log))(customExecutionContext))

  def hardWorkFunction(logRow: String): String = {
    println(s"Start parse $logRow  ${Thread.currentThread().getName}")

    val result = for {
      i <- 1 to 1000
      j <- 1 to 100
      k <- 1 to 100
    } yield math.sqrt(i * j * k)

    println(s"End parse $logRow\n")
    "Success parsed"
  }
}
