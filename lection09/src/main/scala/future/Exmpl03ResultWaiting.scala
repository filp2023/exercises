package future

import scala.concurrent._
import scala.concurrent.duration._

object Exmpl03ResultWaiting extends App {
  // global fork-joined bases EC
  implicit val ec: ExecutionContext = ExecutionContext.global

  val resF = Future(println("executed future"))

  println(Await.result(resF, 10.seconds))

}