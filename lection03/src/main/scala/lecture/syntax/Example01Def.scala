package lecture.syntax

object Example01Def extends App {
  val temp = 1

  def funky1(foo: String, bar: Int): String =
    foo + temp

  def funky2(foo: String, bar: Int): Unit = {
    val a1 = 1
    foo + bar + a1
  }

  def funky3(foo: String, bar: Int) =
    foo + bar

  def funky4(foo: String, bar: Int) {
    foo + bar
  }
}

trait Example01Def {
  def funky5(foo: String, bar: Int): String =
    foo + bar
}

class Example01DefClass {
  def funky5(foo: String, bar: Int): String =
    foo + bar
}