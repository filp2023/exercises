package monad

trait MonadError[F[_], E] extends Monad[F] {
  def raiseError[A](e: E): F[A]

  def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A]
}

object MonadError {
  @inline
  def apply[F[_], E](implicit inst: MonadError[F, E]): MonadError[F, E] =
    inst

  object Syntax {
    implicit class HandleErrorOps[F[_], A](private val fa: F[A]) extends AnyVal {
      def handleErrorWith[E](fa: F[A])(f: E => F[A])(implicit monadError: MonadError[F, E]): F[A] =
        monadError.handleErrorWith(fa)(f)
    }
  }
}
