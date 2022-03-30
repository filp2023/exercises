package part2

import part2.Example04Functors.Functor
import part2.Example04Functors.FunctorOps


object Example05Applicatives extends App {

  //  [A => B] ap [A] = [B]
  //  OR
  //  [A] product [B] = [(A, B)]

  trait Applicative[F[_]] extends Functor[F] {
    def pure[A](x: A): F[A]

    def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]

    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
      ap(map(fa)(a => (b: B) => (a, b)))(fb)
  }

  // def ap[A, B]


  object Applicative {
    @inline
    def apply[F[_]](implicit ev: Applicative[F]): Applicative[F] =
      ev
  }

  object Syntax {
    implicit class ApplicativeOps[F[_], A](private val fa: F[A]) {
      // fa.product(fb)
      def product[B](fb: F[B])(implicit ap: Applicative[F]): F[(A, B)] =
        ap.product(fa, fb)

      def aproduct[B](fb: F[B])(implicit ap: Applicative[F]): F[(A, B)] =
        ap.product(fa, fb)
    }

    implicit class PureOps[A](private val a: A) extends AnyVal {
      // a.pure
      def pure[F[_] : Applicative]: F[A] =
        Applicative[F].pure(a)
    }
  }

  import Syntax._


  // Законы
  // fa.product(fb).product(fc) ~ fa.product(fb.product(fc)) -- associativity
  // pure(()).product(fa) ~ fa                               -- left identity
  // fa.product(pure(())) ~ fa                               -- right identity


  // Инстанс для Option
  implicit val optionApplicative: Applicative[Option] = new Applicative[Option] {
    def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] =
      (fa, ff) match {
        case (Some(value), Some(func)) => Some(func(value))
        case _                         => None
      }

    def pure[A](x: A): Option[A] =
      Some(x)

    def map[A, B](fa: Option[A])(f: A => B): Option[B] =
      fa.map(f)
  }

  val maybeA = 13.pure[Option]
  val maybeB = 37.pure[Option]

  val maybeAandB = maybeA.aproduct(maybeB)


  // Инстанс для List
  implicit val listApplicative: Applicative[List] = new Applicative[List] {
    def ap[A, B](ff: List[A => B])(fa: List[A]): List[B] =
      ff.zip(fa).map { case (func, value) => func(value) }

    def pure[A](x: A): List[A] =
      List(x)

    def map[A, B](fa: List[A])(f: A => B): List[B] =
      fa.map(f)
  }




  val severalA = List(1, 2, 3, 4)
  val severalB = List("one", "two", "three")

  val severalAandB = severalA.aproduct(severalB)
  println(severalAandB)
}
