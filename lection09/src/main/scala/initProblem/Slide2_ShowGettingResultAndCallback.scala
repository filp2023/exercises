package initProblem

import java.util.concurrent.atomic.AtomicReference

import scala.util.control.Breaks.break

object Slide2_ShowGettingResultAndCallback extends App {


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

  val atomicRef = new AtomicReference[String]()
  new Thread(() => atomicRef.set(hardWorkFunction("log1"))).start()

  while (true) {
    val v = atomicRef.get()
    if (v != null) {
      println(s"Getting result: $v")
      break
    }
    else println("Ah, here we go again")
  }


  // Либо callbacks

  val callBack: String => Unit = input => println(s"working with result $input") // atomicRef.set(input)

  new Thread(() => callBack(hardWorkFunction("log1"))).start()

}
