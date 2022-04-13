package initProblem

import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO, IOApp}
import cats.instances.all._
import cats._
import cats.implicits._

import scala.concurrent.ExecutionContext

object Slide6_0_IntroBetterScala extends App {

  implicit val contextShift =
    IO.contextShift(ExecutionContext.fromExecutorService(Executors.newCachedThreadPool()))



  val io1: IO[String] = IO(hardWorkFunction("hello1"))
  val io2: IO[String] = IO(hardWorkFunction("hello2"))
  val io3: IO[String] = IO(hardWorkFunction("hello3"))

  io1.start//(contextShift)

//  io1.handleErrorWith(???)
//  io2.timeout(???)(???, ???)

  val compute = for  {
    fr1 <- io1.start
    fr2 <- io2.start
    fr3 <- io3.start
    r1 <- fr1.join
    r2 <- fr2.join
    r3 <- fr3.join

  } yield r1 + r2 + r3

  println(compute.unsafeRunSync())


  def hardWorkFunction(logRow: String): String = {
    println(s"Start parse $logRow  ${Thread.currentThread().getName}")

    val result = for {
      i <- 1 to 9000
      j <- 1 to 100
      k <- 1 to 100
    } yield math.sqrt(i * j * k)

    println(s"End parse $logRow\n")
    "Success parsed"
  }
}
