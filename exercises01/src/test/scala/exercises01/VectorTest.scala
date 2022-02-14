package exercises01

class VectorTest extends org.scalatest.wordspec.AnyWordSpec {
  val a = new Vector(10.1, 20.0)
  val b = new Vector(20.3, 23.4)

  val error: Double = 0.01

  "Vector" should {
    "support plus operation" in {
      assert(a + b == new Vector(30.4, 43.4))
    }

    "support minus operation" in {
      assert(new Vector(10, 20) - new Vector(-20, 30) == new Vector(30, -10))
    }

    "support multiply on scalar operation" in {
      assert(a * 12 == new Vector(10.1 * 12, 20.0 * 12))
    }

    "support unary minus operation" in {
      assert(-a == new Vector(-10.1, -20.0))
    }

    "support euclideanLength operation" in {
      assert(new Vector(3, 4).euclideanLength == 5)
    }

    "support normalized operation" in {
      val vector = new Vector(10, -10).normalized
      val sin = math.sin(math.Pi / 4)

      assert(Math.abs(vector.x - sin) <= error)
      assert(Math.abs(vector.y + sin) <= error)
    }

    "support toString operation" in {
      assert(new Vector(10.4, 10.5).toString == "Vector(10.4, 10.5)")
    }

    "support fromAngle operation" in {
      val vector = Vector.fromAngle(math.Pi, 10)

      assert(Math.abs(vector.x + 10) <= error)
      assert(Math.abs(vector.y) <= error)
    }

    "support sum operation" in {
      assert(Vector.sum(List(a, b, new Vector(10, 10))) == new Vector(40.4, 53.4))
    }

    "support unapply operation" in {
      val m =
        a match {
          case Vector(x, y) => x * y
        }

      assert(m == 10.1 * 20.0)
    }

    "support equals operation" in {
      assert(new Vector(10.0, 20.0) == new Vector(10.0, 20.0))
    }
  }
}
