package monad

//noinspection OptionEqualsSome,EmptyCheck
object Example00MonadLaws extends App {
  // Left identity: Monad[F].pure(a).flatMap(f) == f(a)
  Some("123").flatMap(_.toLongOption) == "123".toLongOption
  Some("aaa").flatMap(_.toLongOption) == "aaa".toLongOption

  // Right identity: fa.flatMap(Monad[F].pure) == fa
  Some(123).flatMap(Some(_)) == Some(123)
  Option.empty[Int].flatMap(Some(_)) == None

  // Associativity: fa.flatMap(f).flatMap(g) == fa.flatMap(a => f(a).flatMap(g))

  val f: String => Option[String] = str => Some(str + "1")
  val g: String => Option[Long] = _.toLongOption

  Some("123").flatMap(f).flatMap(g) == Some("123").flatMap(a => f(a).flatMap(g))
}
