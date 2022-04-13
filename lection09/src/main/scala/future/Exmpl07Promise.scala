package future

import scala.concurrent._
import scala.io.StdIn
import scala.util.Success
import scala.concurrent.duration._

object Exmpl07Promise extends App {
  val promise01 = Promise[String]
  val future01 = promise01.future

  val th = new Thread(new Runnable {
    override def run(): Unit =
      promise01.complete(
        Success(
          StdIn.readLine()
        )
      )
  })
  th.setDaemon(true)
  th.start()

  val promise02 = Promise[String]
  promise02.completeWith(future01)

  val future02 = promise02.future

  println(Await.result(future02.map(x => x * 10)(ExecutionContext.global), 10.seconds))
}

// try2
object Exmpl07PromiseA extends App {

  // typical callback based async IO
  def readLine(cb: String => Unit): Unit = {
    val th = new Thread(new Runnable {
      override def run(): Unit = {
        cb(StdIn.readLine())
      }
    })
    th.setDaemon(true)
    th.start()
  }

  def read: Future[String] = {
    val promise01 = Promise[String]
    readLine({ x => promise01.complete(Success(x)) })
    promise01.future
  }

  println(Await.result(read.map(x => x * 10)(ExecutionContext.global), 10.seconds))
}