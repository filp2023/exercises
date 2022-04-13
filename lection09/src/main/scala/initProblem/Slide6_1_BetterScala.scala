package initProblem

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import cats.effect.IO

import scala.util.Try

object Slide6_1_BetterScala extends App {

  def oldCallbackApi(log: String)(callback: Try[String] => Unit): Unit =
    callback {
      Try {
        hardWorkFunction(log)
      }
    }

  // а композировать монады мы уже умеем
  def newFutureApi(log: String): IO[String] = IO.asyncF { callback =>
    IO {
      oldCallbackApi(log) {
        in => callback(in.toEither)
      }
    }
  }




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
