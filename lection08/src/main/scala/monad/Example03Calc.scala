package monad

import Monad.Syntax._

object Example03Calc extends App {
  sealed trait Expr
  case class Mul(left: Expr, right: Expr)   extends Expr
  case class Div(left: Expr, right: Expr)   extends Expr
  case class Plus(left: Expr, right: Expr)  extends Expr
  case class Minus(left: Expr, right: Expr) extends Expr
  case class Val(v: Double)                 extends Expr
  case class ArcSin(expr: Expr)             extends Expr

  sealed trait CalculationError
  object CalculationError {
    case object DivisionByZero               extends CalculationError
    case class InvalidArcSinArg(arg: Double) extends CalculationError
  }

  type Calculate[F[_]] = MonadError[F, CalculationError]

  object Calculate {
    @inline
    def apply[F[_]: Calculate](implicit inst: Calculate[F]): Calculate[F] =
      inst
  }

  object Service {
    def calculate[F[_]: Calculate](expr: Expr): F[Double] =
      expr match {
        case Val(v)             => v.pure[F]
        case Mul(left, right)   => binaryOp(calculate(left), calculate(right), _ * _)
        case Plus(left, right)  => binaryOp(calculate(left), calculate(right), _ + _)
        case Minus(left, right) => binaryOp(calculate(left), calculate(right), _ - _)
        case Div(left, right) =>
          for {
            rightValue <- calculate(right)
            res <- rightValue match {
              case 0.0   => Calculate[F].raiseError(CalculationError.DivisionByZero)
              case value => calculate(left).map(_ / value)
            }
          } yield res
        case ArcSin(expr) =>
          for {
            arg <- calculate(expr)
            res <- if (arg <= 1.0 && arg >= -1.0)
              math.asin(arg).pure[F]
            else
              Calculate[F].raiseError(CalculationError.InvalidArcSinArg(arg))
          } yield res
      }

    private def binaryOp[F[_]: Calculate](
        leftF: F[Double],
        rightF: F[Double],
        op: (Double, Double) => Double
    ): F[Double] =
      for {
        left  <- leftF
        right <- rightF
      } yield op(left, right)
  }

  object Instances {
    implicit def eitherMonadError[E]: MonadError[Either[E, *], E] = new MonadError[Either[E, *], E] {
      def raiseError[A](e: E): Either[E, A] =
        Left(e)

      def pure[A](a: A): Either[E, A] =
        Right(a)

      def handleErrorWith[A](fa: Either[E, A])(f: E => Either[E, A]): Either[E, A] =
        fa.left.flatMap(f)

      def flatMap[A, B](fa: Either[E, A])(f: A => Either[E, B]): Either[E, B] =
        fa.flatMap(f)
    }

    sealed trait OwnContainer[+A]
    case class Error(err: CalculationError) extends OwnContainer[Nothing]
    case class Success[A](value: A)         extends OwnContainer[A]

    implicit val ownContainerMonadError: Calculate[OwnContainer] = new MonadError[OwnContainer, CalculationError] {
      def raiseError[A](e: CalculationError): OwnContainer[A] =
        Error(e)

      def pure[A](a: A): OwnContainer[A] =
        Success(a)

      def handleErrorWith[A](fa: OwnContainer[A])(f: CalculationError => OwnContainer[A]): OwnContainer[A] =
        fa match {
          case Error(err)          => f(err)
          case success: Success[A] => success
        }

      def flatMap[A, B](fa: OwnContainer[A])(f: A => OwnContainer[B]): OwnContainer[B] =
        fa match {
          case Success(value) => f(value)
          case err: Error     => err
        }
    }
  }

  import Instances._
  import Service._

  println(calculate[Either[CalculationError, *]](ArcSin(Val(1.0))))
  println(calculate[Either[CalculationError, *]](ArcSin(Val(2.0))))
  println(calculate[Either[CalculationError, *]](ArcSin(Div(Val(2.0), Val(0.0)))))

  println()

  println(calculate[OwnContainer](ArcSin(Val(1.0))))
  println(calculate[OwnContainer](ArcSin(Val(2.0))))
  println(calculate[OwnContainer](ArcSin(Div(Val(2.0), Val(0.0)))))
}
