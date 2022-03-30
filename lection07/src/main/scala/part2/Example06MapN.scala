package part2

import Example05Applicatives._
import Example05Applicatives.Syntax._

object Example06MapN extends App {
  implicit class Tuple2Syntax[F[_], A, B](private val tuple: (F[A], F[B])) extends AnyVal {
    def mapN[Z](f: (A, B) => Z)(implicit applicative: Applicative[F]): F[Z] =
      applicative.map(applicative.product(tuple._1, tuple._2)) { case (a, b) => f(a, b) }
  }

  implicit class Tuple3Syntax[F[_], A, B, C](private val tuple: (F[A], F[B], F[C])) extends AnyVal {
    def mapN[Z](f: (A, B, C) => Z)(implicit applicative: Applicative[F]): F[Z] =
      applicative.map(applicative.product(applicative.product(tuple._1, tuple._2), tuple._3)) {
        case ((a, b), c) => f(a, b, c)
      }
  }

  val maybeA = 13.pure[Option]
  val maybeB = 37.pure[Option]

  (maybeA, maybeB).mapN { (a, b) =>
    a + b
  }
}
