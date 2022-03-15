package fintech.lecture.examples

import scala.collection.immutable.LinearSeq
import scala.collection.mutable

object Example10Hier extends App {
//  Iterable("x", "y", "z").map(???).flatMap(???)
//  Map("x" -> 24, "y" -> 25, "z" -> 26).map(???).flatMap(???)
//  Set(1, 2, 3).map(???).flatMap(???)
//  mutable.Buffer(5, 4, 5).map(???).flatMap(???)
//  IndexedSeq(1.0, 2.0).map(???).flatMap(???)
//  LinearSeq("a, b, c").map(???).flatMap(???)

  val iterable: Iterable[Int] = List(1, 2, 3)

  val indexedSeq: IndexedSeq[String] = Vector("one", "two", "three")

  val immutableList = List(1, 2, 3)
  val mutableList   = mutable.ListBuffer(1, 2, 3)

  val immutableMap = Map(1 -> "one", 2 -> "two", 3 -> "three")

  val mutableMap = mutable.Map(1 -> "one", 2 -> "two", 3 -> "three")

  val newIter: Iterable[String] = immutableMap.map { kv =>
    kv._1.toString
  }
  val newMap: Map[String, String] = immutableMap.map(kv => kv._1.toString -> kv._2)

  val seq: Seq[Int] = Seq(1, 2, 3)
  println(seq(1))
}

object Ops extends App {
  val iterable: Iterable[Int] = List(1, 2, 3)

  println(iterable.map(_ + 2))
  println(iterable.flatMap(x => x :: x + 2 :: Nil))
  println(iterable.filter(x => x > 2))
  println(iterable.filterNot(x => x > 2))
  println(iterable.groupBy(x => x > 2))
  println(iterable.partition(x => x > 2))

}