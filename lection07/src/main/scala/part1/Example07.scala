package part1

object Example07 {
  trait SomeTypeClass[A] {
    def method1A(a: A, p1: Int, p2: Float): String
    def methodNA(x: A, y: A): A
    def factory: A
  }

  case class SomeClass(i: Int, f: Float)

  val typeClassInstance = new SomeTypeClass[SomeClass] {
    override def method1A(a: SomeClass, p1: Int, p2: Float): String = ???
    override def methodNA(x: SomeClass, y: SomeClass): SomeClass = ???
    override def factory: SomeClass = ???
  }
}
