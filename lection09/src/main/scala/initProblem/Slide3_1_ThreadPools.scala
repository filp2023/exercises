package initProblem

import java.util.concurrent.{ExecutorService, Executors, ForkJoinPool}

object Slide3_1_ThreadPools extends App {

  val fixedThreadPool: ExecutorService =
    Executors.newFixedThreadPool(2)

  val cachedThreadPool: ExecutorService =
    Executors.newCachedThreadPool()

  val forkJoin: ExecutorService =
    new ForkJoinPool()


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


  val logs = List.tabulate(4)( i => s"log$i")

//  logs.foreach(log => cachedThreadPool.execute(() => hardWorkFunction(log)))
//  logs.foreach(log => forkJoin.execute(() => hardWorkFunction(log)))
  logs.foreach(log => fixedThreadPool.execute(() => hardWorkFunction(log)))

// Shutdown hooks
//  cachedThreadPool.shutdown()
//  forkJoin.shutdown()
  fixedThreadPool.shutdown()






  println(cachedThreadPool.submit(() => hardWorkFunction("test")).get)
//  cachedThreadPool.execute()
}
