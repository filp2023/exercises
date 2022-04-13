package future

import scala.concurrent._
import scala.concurrent.duration._
import scala.io.StdIn

object Exmpl04Combinators extends App {
  // global fork-joined bases EC
  implicit val ec: ExecutionContext = ExecutionContext.global

  def future(i: Int): Future[Int] = Future(i)

  future(1).map(_ + 2)
  future(1).flatMap(i => future(i + 2))
  future(1).zip(future(2))

  val listFuture: List[Future[Int]] = List(future(1), future(2))

  val futureList: Future[List[Int]] = Future.sequence(listFuture)

  val traverse: Future[List[Int]] = Future.traverse(List(1, 2, 3))(future)

  val fSum = for {
    i1 <- future(1)
    i2 <- future(1)
    i3 <- future(1)
  } yield i1 + i2 + i3



}