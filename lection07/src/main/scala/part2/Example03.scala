package part2

object Example03 {
  // type-alias and kind-projector

  def combine3[F[_]](objects: F[Int]): Int = ???

  type StringReader[A] = String => A

  val someIntReader: StringReader[Int] = s => s.toInt + 42

  combine3(someIntReader)


  combine3[Function1[String, *]](s => s.toInt * 31337)

}
