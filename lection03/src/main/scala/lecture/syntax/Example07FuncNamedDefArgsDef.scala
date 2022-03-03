package lecture.syntax

object Example07FuncNamedDefArgsDef {
  def int42 = 42

  //we can use const
  def funky0(foo: String, bar: Int = 0): String = {
    foo + bar
  }

  //but also we can use not const
  def funky1(foo: String, bar: Int = int42): String = {
    foo + bar
  }

  funky1(bar = 13, foo = "oppa") == "oppa13"
  funky1(foo = "oppa") == "oppa42"
  funky1("oppa") == "oppa42"
}
