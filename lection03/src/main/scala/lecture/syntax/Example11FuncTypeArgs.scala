package lecture.syntax

object Example11FuncTypeArgs extends App {
  def identityAny(a: Any): Any = a
  def identityStr(a: String): String = a
  def identityInt(a: Int): Int = a
  //...

  def identity[T](a: T): T = a
  identity[Int](1)
}
