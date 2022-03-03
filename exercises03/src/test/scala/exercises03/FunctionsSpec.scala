package exercises03

import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class FunctionsSpec extends AnyWordSpec with ScalaCheckDrivenPropertyChecks {
  "Functions.curry" should {
    "curry function" in {
      forAll { (f: (Int, Int) => Int, a: Int, b: Int) =>
        assert(Functions.curry(f)(a)(b) == f(a, b))
      }
    }
  }

  "Functions.uncurry" should {
    "uncurry function" in {
      forAll { (f: Int => Int => Int, a: Int, b: Int) =>
        assert(Functions.uncurry(f)(a, b) == f(a)(b))
      }
    }
  }
}
