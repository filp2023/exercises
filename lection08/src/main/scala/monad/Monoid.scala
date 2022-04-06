package monad

trait Monoid[A] {
  def combine(x: A, y: A): A

  def empty: A
}

object Monoid {
  @inline
  def apply[A](implicit inst: Monoid[A]): Monoid[A] =
    inst

  object Syntax {
    implicit class CombineOps[A](private val x: A) extends AnyVal {
      def |+|(y: A)(implicit monoid: Monoid[A]): A =
        monoid.combine(x, y)
    }
  }
}
