package part2

import scala.annotation.tailrec

object Example01HKT {
  // HKT, F[_]

  def sumVector(ints: Vector[Int]): Int = ints.foldLeft(0)(_ + _)

  def sumList(ints: List[Int]): Int = ints.foldLeft(0)(_ + _)

  // решение с subtype-полиморфизмом
  def sumSeq(ints: Seq[Int]): Int = ints.foldLeft(0)(_ + _)


  // опять нарываемся на проблему с расширением
  object MyFastSequences {
    sealed trait MyList[+A]
    case object Nil extends MyList[Nothing]
    final case class Cons[A](h: A, t: MyList[A]) extends MyList[A]

    implicit class MyListOps[A](private val list: MyList[A]) extends AnyVal {
      def ::[B >: A](h: B): MyList[B] = Cons(h, list)
    }
  }
  import MyFastSequences._

//  won't compile
//  sumSeq(1 :: 2 :: Nil)

  def sumMyList(ints: MyList[Int]): Int = {
    @tailrec
    def sumLeft(acc: Int, list: MyList[Int]): Int = list match {
      case Nil => acc
      case Cons(h, t) => sumLeft(acc + h, t)
    }

    sumLeft(0, ints)
  }


}
