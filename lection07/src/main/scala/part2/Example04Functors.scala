package part2

object Example04Functors extends App {

//  [A] map (A => B) = [B]

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  object Functor {
    @inline
    def apply[F[_]](implicit ev: Functor[F]): Functor[F] = ev
  }

  implicit class FunctorOps[F[_], A](private val fa: F[A]) extends AnyVal {
    // fa.map(f)
    def map[B](f: A => B)(implicit functor: Functor[F]): F[B] =
      functor.map(fa)(f)
  }

  // Законы
  // fa.map(a => a) = a                       -- identity
  // fa.map(f).map(g) = fa.map(a => f(g(a)))  -- composition


  // Пример функции, использующей Functor
  def plus1[F[_]: Functor](f: F[Int]): F[Int] =
    f.map(_ + 1)


  // Инстанс для Option
  implicit val optionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](fa: Option[A])(f: A => B): Option[B] =
      fa match {
        case Some(value) => Some(f(value))
        case None        => None
      }
  }


  println("Option is a Functor!")
  println(plus1[Option](Some(10))) // Some(11)
  println(plus1[Option](None)) // None


  // Инстанс для List
  implicit val listFunctor: Functor[List] = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] =
      fa.map(f)
  }

  println("List is a Functor!")
  println(plus1(List(1, 2))) // List(2, 3)


  // Identity
  type Id[A] = A

  implicit val idFunctor: Functor[Id] = new Functor[Id] {
    def map[A, B](fa: A)(f: A => B): B =
      f(fa)
  }

  println("Id is a Functor!")
  println(plus1[Id](10)) // 11


  // Контексты с несколькими параметрами: Reader, Either, etc
  type StringReader[A] = Function1[String, A]

  implicit val stringReaderFunctor: Functor[StringReader] = new Functor[StringReader] {
    override def map[A, B](fa: StringReader[A])(f: A => B): StringReader[B] =
      fa.andThen(f)
  }

  val complexReader: StringReader[Int] =
    s => s.toInt + 42

  val mappedReader: String => Int =
    plus1(complexReader)

  println("StringReader is a Functor!")
  println(mappedReader("37"))


  // Kind-projector
  // StringReader = Function1[From, *]
  implicit def functionFunctor[From]: Functor[Function1[From, *]] = new Functor[Function1[From, *]] {
    def map[A, B](fa: From => A)(f: A => B): From => B =
      fa.andThen(f)
  }

  val added = plus1[Function[Int, *]](_ + 1)
  println("Reader is a Functor!")
  println(added(0)) // 2


  // Не все F[_] могут быть Functor!
  type StringWriter[A] = Function1[A, String]
//  implicit val stringWriter: Functor[StringWriter] = new Functor[StringWriter] {
//    override def map[A, B](fa: A => String)(f: A => B): StringWriter[B] =
//      // impossible
//  }

}
