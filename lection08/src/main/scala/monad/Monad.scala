package monad

trait Monad[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def pure[A](a: A): F[A]

  def map[A, B](fa: F[A])(f: A => B): F[B] =
    flatMap(fa)(x => pure(f(x)))
}

object Monad {
  @inline
  def apply[F[_]](implicit inst: Monad[F]): Monad[F] = inst

  object Syntax {
    implicit class MonadOps[F[_], A](private val fa: F[A]) extends AnyVal {
      def map[B](f: A => B)(implicit monad: Monad[F]): F[B] =
        monad.map(fa)(f)

      def flatMap[B](f: A => F[B])(implicit monad: Monad[F]): F[B] =
        monad.flatMap(fa)(f)
    }

    implicit class PureOps[A](private val a: A) extends AnyVal {
      def pure[F[_]](implicit monad: Monad[F]): F[A] =
        monad.pure(a)
    }
  }
}