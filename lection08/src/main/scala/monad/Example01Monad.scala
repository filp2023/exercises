package monad

import Monad.Syntax._

object Example01Monad extends App {
  case class User(id: String, name: String, surname: String)

  object Service {
    def loadUser[F[_]: Monad](tag: String): F[User] =
      for {
        id      <- idByTag(tag)
        name    <- nameById(id)
        surname <- surnameById(id)
      } yield User(id, name, surname)

    def idByTag[F[_]: Monad](tag: String): F[String] =
      s"$tag-id".pure[F]

    def nameById[F[_]: Monad](id: String): F[String] =
      id match {
        case "123-id" => "Alice".pure[F]
        case "456-id" => "Bob".pure[F]
        case _        => "Carol".pure[F]
      }

    def surnameById[F[_]: Monad](id: String): F[String] =
      id match {
        case "123-id" => "Johnson".pure[F]
        case "657-id" => "Anderson".pure[F]
        case _        => "Smith".pure[F]
      }
  }

  object Instances {
    implicit val listMonad: Monad[List] = new Monad[List] {
      def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] =
        fa.flatMap(f)

      def pure[A](a: A): List[A] =
        List(a)
    }

    implicit val optionMonad: Monad[Option] = new Monad[Option] {
      def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] =
        fa.flatMap(f)

      def pure[A](a: A): Option[A] =
        Some(a)
    }

    implicit def eitherMonad[E]: Monad[Either[E, *]] = new Monad[Either[E, *]] {
      override def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] =
        fa.flatMap(f)

      override def pure[A](a: A): Either[E, A] =
        Right(a)
    }
  }

  import Instances._
  println(Service.loadUser[Option]("123"))
  println(Service.loadUser[List]("456"))
  println(Service.loadUser[Either[String, *]]("657"))
}
