package exercises05

import exercises05.Combinators.react
import org.scalatest.wordspec.AnyWordSpec

class CombinatorsSpec extends AnyWordSpec {
  "Combinators.react" should {
    "react" in {
      assert(react("hH") == "")
      assert(react("gG") == "")
      assert(react("fF") == "")
      assert(react("eE") == "")
      assert(react("egGE") == "")
      assert(react("EGge") == "")
      assert(react("EgGe") == "")
      assert(react("egHfFhGE") == "")
      assert(react("hefEgGeGFEgGgeHE") == "hefGFEgeHE")
    }

    "don't react" in {
      assert(react("h") == "h")
      assert(react("g") == "g")
      assert(react("f") == "f")
      assert(react("e") == "e")
      assert(react("ff") == "ff")
      assert(react("ee") == "ee")
      assert(react("hh") == "hh")
      assert(react("gg") == "gg")
      assert(react("FF") == "FF")
      assert(react("EE") == "EE")
      assert(react("HH") == "HH")
      assert(react("GG") == "GG")
      assert(react("fhge") == "fhge")
      assert(react("FHGE") == "FHGE")
      assert(react("egge") == "egge")
      assert(react("EGGE") == "EGGE")
    }

    "work with empty string" in {
      assert(react("") == "")
    }
  }
}
