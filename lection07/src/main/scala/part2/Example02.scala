package part2

import scala.annotation.tailrec

object Example02 {
  // HKT, F[_]

  trait Foldable[F[_]] {
    def foldLeft[A, B](fa: F[A], b: B)(f: (B, A) => B): B
  }

  object Foldable {
    @inline
    def apply[F[_]](implicit foldable: Foldable[F]): Foldable[F] = foldable
  }

  def sumInts[F[_]: Foldable](ints: F[Int]): Int =
    Foldable[F].foldLeft(ints, 0)(_ + _)


  // для листа
  implicit val listFoldable: Foldable[List] = new Foldable[List] {
    override def foldLeft[A, B](fa: List[A], b: B)(f: (B, A) => B): B =
      fa.foldLeft(b)(f)
  }

  sumInts(List(1, 2, 3))



  // для вектора
  implicit val vectorFoldable: Foldable[Vector] = new Foldable[Vector] {
    override def foldLeft[A, B](fa: Vector[A], b: B)(f: (B, A) => B): B =
      fa.foldLeft(b)(f)
  }

  sumInts(Vector(1, 2, 3))



  // для MyList
  import part2.Example01HKT.MyFastSequences._
  implicit val myListFoldable: Foldable[MyList] = new Foldable[MyList] {
    override def foldLeft[A, B](fa: MyList[A], b: B)(f: (B, A) => B): B = {
      @tailrec
      def foldLeftRec(acc: B, list: MyList[A]): B = list match {
        case Nil => acc
        case Cons(h, t) => foldLeftRec(f(acc, h), t)
      }

      foldLeftRec(b, fa)
    }
  }

  sumInts(1 :: 2 :: Nil)

}
