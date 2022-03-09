package exercises04

import exercises04.Machine.Input.{Coin, Turn}
import org.scalatest.wordspec.AnyWordSpec

class MachineSpec extends AnyWordSpec {
  "Machine.run" should {
    "если в закрытый аппарат вставляется монета (Coin), то аппартат открывается" in {
      assert(
        Machine.run(Machine(locked = true, 1, 0), Coin :: Nil) ==
          (Machine(locked = false, 1, 1), Nil)
      )
    }

    "если повернуть ручку (Turn) у открытого аппарата, то выйдет барбариска, и аппарат закроется" in {
      assert(
        Machine.run(Machine(locked = false, 1, 0), Turn :: Nil) ==
          (Machine(locked = true, 0, 0), Nil)
      )
    }

    "если в аппарате кончились барбариски, то он никак не реагирует. в этом случае надо вернуть список оставшихся Inputs и закончить" in {
      assert(
        Machine.run(Machine(locked = false, 0, 0), Coin :: Nil) ==
          (Machine(locked = false, 0, 0), Coin :: Nil)
      )

      assert(
        Machine.run(Machine(locked = true, 0, 0), Turn :: Nil) ==
          (Machine(locked = true, 0, 0), Turn :: Nil)
      )

      assert(
        Machine.run(Machine(locked = true, 0, 42), Turn :: Coin :: Coin :: Turn :: Nil) ==
          (Machine(locked = true, 0, 42), Turn :: Coin :: Coin :: Turn :: Nil)
      )
    }

    "другие действия приводят к пропуску Input" in {
      assert(
        Machine.run(Machine(locked = false, 1, 0), List.fill(100000)(Coin)) ==
          (Machine(locked = false, 1, 0), Nil)
      )

      assert(
        Machine.run(Machine(locked = true, 1, 0), List.fill(100000)(Turn)) ==
          (Machine(locked = true, 1, 0), Nil)
      )
    }

    "если Input кончился, то заканчиваем" in {
      val input = (Machine(locked = false, 10, 42), Nil)
      assert((Machine.run _).tupled(input) == input)
    }

    "длинная игра" in {
      val count = 1000000
      assert(
        Machine.run(Machine(locked = true, count, 0), List.fill(count)(List(Coin, Turn)).flatten) ==
          (Machine(locked = true, 0, count), Nil)
      )
    }
  }
}
