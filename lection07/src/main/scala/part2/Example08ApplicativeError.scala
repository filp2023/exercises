package part2

import part2.Example05Applicatives.Applicative
import part2.Example05Applicatives.Syntax._

import scala.util.{Failure, Success, Try}

object Example08ApplicativeError extends App {
  trait ApplicativeError[F[_], E] extends Applicative[F] {
    def raiseError[A](e: E): F[A]

    def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A]
  }

  object ApplicativeError {
    @inline
    def apply[F[_], E](implicit inst: ApplicativeError[F, E]): ApplicativeError[F, E] =
      inst
  }

  object Syntax {
    implicit class HandleErrorSyntax[F[_], A](private val fa: F[A]) extends AnyVal {
      // fa.handleErrorWith(f)
      def handleErrorWith[E](f: E => F[A])(implicit applicativeError: ApplicativeError[F, E]): F[A] =
        applicativeError.handleErrorWith(fa)(f)
    }

    implicit class RaiseErrorSyntax[E](private val e: E) extends AnyVal {
      // e.raiseError
      def raiseError[F[_], A](implicit F: ApplicativeError[F, _ >: E]): F[A] =
        F.raiseError(e)
    }
  }
  import Syntax._



  // usage example
  type ApplicativeThrowable[F[_]] = ApplicativeError[F, Throwable]

  def loadTag[F[_]: ApplicativeThrowable](id: Int): F[String] =
    if (id < 5) s"LocalTag-$id".pure
    else if (id > 6)
      s"ExternalTag-$id".pure
    else
      new RuntimeException("No such tag").raiseError


  {
    // Инстанс для Try
    implicit val tryApplicativeError: ApplicativeError[Try, Throwable] = new ApplicativeError[Try, Throwable] {
      override def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa.map(f)

      override def pure[A](x: A): Try[A] = Success(x)

      override def ap[A, B](ff: Try[A => B])(fa: Try[A]): Try[B] = fa match {
        case Failure(exception) => Failure(exception)
        case Success(a) => ff match {
          case Failure(exception) => Failure(exception)
          case Success(f) => Success(f(a))
        }
      }

      override def raiseError[A](e: Throwable): Try[A] = Failure(e)

      override def handleErrorWith[A](fa: Try[A])(f: Throwable => Try[A]): Try[A] =
        fa.recoverWith {
          case th => f(th)
        }
    }

    println(loadTag[Try](1))

    println(loadTag[Try](7))

    println(loadTag[Try](6))

    println(loadTag[Try](6).handleErrorWith[Throwable](err => Try(s"ErrorTag: ${err.getMessage}")))
  }
}
