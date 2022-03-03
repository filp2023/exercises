package lecture.syntax

object Example06FuncNamedArgsDef {
  def funky1(foo: String, bar: Int): String = {
    foo + bar
  }

  funky1(bar = 42, foo = "oppa")
}
