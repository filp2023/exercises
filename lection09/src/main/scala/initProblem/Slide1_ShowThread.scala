package initProblem

object Slide1_ShowThread extends App {

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


  val logs = List.tabulate(4)( i => s"log$i")

  logs.foreach(log => new Thread(() => hardWorkFunction(log)).start())


//  new Thread(
//      new Runnable {
//        def run() {
//          // Наше вычисление
//          println("hello world")
//        }
//    }
//  )
//    .start() // Запустили вычисление

}
