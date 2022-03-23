package examples

object Example05Usage2c extends App {

  implicit class DoubleOps(private val d: Double) extends AnyVal {
    def square: Double = d * d
    def cube: Double = d * d * d
  }

  object ReplacedByScalaCompiler {
    class DoubleOps(private val d: Double) extends AnyVal {
      def square: Double = d * d
      def cube: Double = d * d * d
    }

    implicit def DoubleOps(d: Double): DoubleOps = new DoubleOps(d)
  }

  val d = 3d

  println(d.square)
  println(d.cube)

}
