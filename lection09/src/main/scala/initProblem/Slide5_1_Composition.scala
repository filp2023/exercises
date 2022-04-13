package initProblem

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}

object Slide5_1_Composition extends App {

  implicit val customExecutionContext =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  def future(log: String) = Future(hardWorkFunction(log))(customExecutionContext)

  val future1 = future("1")
  val future2 = future("2")
  val future3 = future("3")

  val f: Future[String] = for {
    r1 <- future1
    r2 <- future2
    r3 <- future3
  } yield r1 + r2 + r3

  future1.recover {
    case _: Throwable => "recover"
  }

  future1.recoverWith {
    case _: Throwable => Future("recover")
  }

  future1.transform(identity, _ => new RuntimeException)

  val listF: List[Future[String]] = List(future1, future2, future3)

  val fList1: Future[List[String]] = Future.sequence(listF)

  val fList2: Future[List[String]] = Future.traverse(List("log1", "log2", "log3"))(future)





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
