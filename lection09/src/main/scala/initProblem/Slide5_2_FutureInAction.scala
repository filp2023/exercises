package initProblem

import java.util.concurrent.{ExecutorService, Executors}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.Try

object Slide5_2_FutureInAction extends App {

  implicit val customExecutionContext: ExecutorService =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  // а как композировать ? А никак. callback hell
  def oldCallbackApi(log: String)(callback: Try[String] => Unit): Unit =
    callback {
      Try {
        hardWorkFunction(log)
      }
    }

  // а композиравать фьючи мы уже умеем
  def newFutureApi(log: String): Future[String] = {
    val promise = Promise[String]
    execute(() => oldCallbackApi(log)(promise.complete))
    promise.future
  }


  def execute(run: () => Unit): Unit =
    customExecutionContext.execute(() => run())






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
