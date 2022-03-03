package lecture.syntax

object Example10FuncGrouppedArgs extends App {
  def int42 = 42

  def funky1(foo: String, kek: Double = 1.0)(bar: Int = int42): String = {
    foo + bar
  }

  println(funky1("foo")(22))
  println(funky1(foo = "oppa")(bar = 13))
}
