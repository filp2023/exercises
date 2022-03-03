package lecture.syntax

object Example08FuncByNameArgs extends App {

  object HOF {

    def hof(x: Int, lazyY: () => Int): Int = if (x > 5) x + lazyY() else x

    def hof2(x: Int, lazyY: => Int): Int = if (x > 5) x + lazyY else x
  }






  def int42 = 42

  def funky(foo: String, bar: Int = int42): String = {
    println("funky")
    foo + bar
  }

  def funkyByName(foo: => String): String = {
    println("funkyByName 1")
    foo
    println("funkyByName 2")
    foo
  }

  def f = 8
  println("res1 :" + funkyByName(funky(bar = f, foo = "oppa")))

}
