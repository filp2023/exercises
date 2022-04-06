package monad

import Monad.Syntax._

import scala.util.{Failure, Success, Try}

object Example02MonadError extends App {
  case class User(id: String, name: String, surname: String)

  type MonadThrow[F[_]] = MonadError[F, Throwable]

  object MonadThrow {
    @inline
    def apply[F[_]](implicit inst: MonadThrow[F]): MonadThrow[F] =
      inst
  }

  object Service {
    def loadUser[F[_]: MonadThrow](tag: String): F[User] =
      for {
        id      <- idByTag(tag)
        name    <- nameById(id)
        surname <- surnameById(id)
      } yield User(id, name, surname)

    def idByTag[F[_]: MonadThrow](tag: String): F[String] =
      s"$tag-id".pure[F]

    def nameById[F[_]: MonadThrow](id: String): F[String] =
      id match {
        case "123-id" => "Alice".pure[F]
        case "456-id" => "Bob".pure[F]
        case _        => MonadThrow[F].raiseError(new RuntimeException("Name not found"))
      }

    def surnameById[F[_]: MonadThrow](id: String): F[String] =
      id match {
        case "123-id" => "Johnson".pure[F]
        case "657-id" => "Anderson".pure[F]
        case _        => MonadThrow[F].raiseError(new RuntimeException("Surname not found"))
      }
  }

  object Instances {
    implicit val eitherMonadThrow: MonadThrow[Either[Throwable, *]] = new MonadError[Either[Throwable, *], Throwable] {
      def raiseError[A](e: Throwable): Either[Throwable, A] =
        Left(e)

      def pure[A](a: A): Either[Throwable, A] =
        Right(a)

      def handleErrorWith[A](fa: Either[Throwable, A])(f: Throwable => Either[Throwable, A]): Either[Throwable, A] =
        fa.left.flatMap(f)

      def flatMap[A, B](fa: Either[Throwable, A])(f: A => Either[Throwable, B]): Either[Throwable, B] =
        fa.flatMap(f)
    }

    implicit val tryMonadThrow: MonadThrow[Try] = new MonadError[Try, Throwable] {
      def raiseError[A](e: Throwable): Try[A] =
        Failure(e)

      def pure[A](a: A): Try[A] =
        Success(a)

      def handleErrorWith[A](fa: Try[A])(f: Throwable => Try[A]): Try[A] =
        fa.recoverWith { case e => f(e) }

      def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] =
        fa.flatMap(f)
    }
  }

  import Instances._
  println(Service.loadUser[Either[Throwable, *]]("123"))
  println(Service.loadUser[Either[Throwable, *]]("456"))
  println(Service.loadUser[Either[Throwable, *]]("657"))

  println(Service.loadUser[Try]("123"))
  println(Service.loadUser[Try]("456"))
  println(Service.loadUser[Try]("657"))
}
