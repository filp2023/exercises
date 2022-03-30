package part1

object Example06FunctionVariation {
  trait SomeTrait {
    def method1A(p1: Int, p2: Float): String
  }

  class SomeClass extends SomeTrait {
    override def method1A(p1: Int, p2: Float): String = ???
    // (this: SomeClass)(p1: Int, p2: Float): String
  }

  // Сложно делать методы с n A-параметрами
  // Сложно делать методы, возвращающие А
  // Сложно делать методы-фабрики
}
