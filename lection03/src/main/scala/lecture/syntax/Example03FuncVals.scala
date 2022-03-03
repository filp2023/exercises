package lecture.syntax

object Example03FuncVals extends App {
  // f1 is function value that on call returns
  // another function value
  val f1 = (x: Int => Int) => ((z: Int, y: Int) => x(z) + y)


  println(f1)
  println(f1(x => x + 1))
  println(f1(x => x + 1)(3, 3))
}
