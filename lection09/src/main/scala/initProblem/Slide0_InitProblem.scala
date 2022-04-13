package initProblem

object Slide0_InitProblem extends App {

  def hardWorkFunction(logRow: String): Unit = {
    println(s"Start parse $logRow  ${Thread.currentThread().getName}")

    val result = for {
      i <- 1 to 9000
      j <- 1 to 100
      k <- 1 to 100
    } yield math.sqrt(i * j * k)

    println(s"End parse $logRow\n")
  }


  val logs = List.tabulate(2)( i => s"log$i")

  logs.foreach(hardWorkFunction)

}
