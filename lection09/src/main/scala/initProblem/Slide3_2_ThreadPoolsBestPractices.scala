package initProblem

import java.net.ServerSocket
import java.util.concurrent.{Executors, RecursiveTask}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object Slide3_2_ThreadPoolsBestPractices extends App {
  import java.util.concurrent._
  import scala.concurrent.duration._

  def sayHiInOneSec:Runnable = {
    val t = new Timer
    val snooze: Duration = 1.seconds
    new Runnable {
      def run {
        while(true) {
          sleep(snooze)
          println(s"Hi from $currentThread after sleeping $snooze, delay of ${t.stop - snooze}")
        }
      }
    }
  }

  def readFromSocket(port: Int): Runnable = {
    new Runnable {
      def run {
        val serverSocket = new ServerSocket(port)
        while (true) {
          println("WAITING CONNECTION")
          // Здесь будет блокировка, пока не произойдет соединение.
          val socket = serverSocket.accept()
          socket.close()
        }
      }
    }
  }

  def submitN(pool: ExecutorService, n: Int) {
    for (_ <- 1 to n) {
      pool.execute(sayHiInOneSec)
    }
  }

  def poolStats(es: ExecutorService) = es match {
    case tpe: ThreadPoolExecutor =>
      println(s"Core Pool Size: ${tpe.getCorePoolSize}")
      println(s"Max  Pool Size: ${tpe.getMaximumPoolSize}")
      println(s"Queue Capacity: ${tpe.getQueue.remainingCapacity + tpe.getQueue.size}")
      println(s"Running Threads: ${tpe.getPoolSize}")
      println(s"Active  Threads: ${tpe.getActiveCount}")
      println(s"Waiting Tasks  : ${tpe.getQueue.size}")
    case _ => "Sorry, I can't dig around in a " + es.getClass.getName
  }

//  poolStats(Executors.newCachedThreadPool)
  //Core Pool Size: 0
  //Max  Pool Size: 2147483647
  //Queue Capacity: 0
  //Running Threads: 0
  //Active  Threads: 0
  //Waiting Tasks  : 0
//  poolStats(Executors.newFixedThreadPool(2))
  //Core Pool Size: 2
  //Max  Pool Size: 2
  //Queue Capacity: 2147483647
  //Running Threads: 0
  //Active  Threads: 0
  //Waiting Tasks  : 0

  val coreOfThree = Executors.newFixedThreadPool(3)
//  coreOfThree.execute(readFromSocket(3333))
//  coreOfThree.execute(readFromSocket(4444))
//  coreOfThree.execute(readFromSocket(5555))
////
//  submitN(coreOfThree, 2)
//  poolStats(coreOfThree)


  // Проблема!!!! Закончились потоки в пуле
  // Для блокирующих операций нужен отдельный тредпул

  val blockingThreadPool = Executors.newCachedThreadPool()
  blockingThreadPool.execute(readFromSocket(3333))
  blockingThreadPool.execute(readFromSocket(4444))
  blockingThreadPool.execute(readFromSocket(5555))

  submitN(coreOfThree, 3)
  poolStats(coreOfThree)







  def now:Long = (new java.util.Date).getTime

  class Timer {
    val then = now
    def stop: Duration = (now - then).millis
    override def toString = (s"Timer started $stop ago")
  }

  def sleep(d: Duration) { Thread.sleep(d.toMillis)}
  def currentThread = Thread.currentThread.getName

}