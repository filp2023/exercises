package initProblem

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object Slide4_ExecutionContext extends App {

  implicit val defaultGlobal: ExecutionContextExecutor =
    scala.concurrent.ExecutionContext.global // by default ForkJoinPool

  val customExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  customExecutionContext.execute(() => hardWorkFunction("test"))

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
