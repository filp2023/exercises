package exercises05

import org.scalatest.wordspec.AnyWordSpec

class DiffListSpec extends AnyWordSpec {
  "DiffList" should {
    "work" in {
      assert(DiffList[Int].toList == Nil)
      assert(DiffList.singletonList(1).toList == 1 :: Nil)
      assert(
        DiffList[Int].append(4 :: 5 :: 6 :: Nil).prepend(1 :: 2 :: 3 :: Nil).toList == 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil
      )
      assert(
        DiffList[Int]
          .append(4 :: 5 :: 6 :: Nil)
          .prepend(1 :: 2 :: 3 :: Nil)
          .withFilter(_ => true)
          .toList == 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil
      )
      assert(DiffList[Int].append(4 :: 5 :: 6 :: Nil).prepend(1 :: 2 :: 3 :: Nil).withFilter(_ => false).toList == Nil)
      assert(
        DiffList[Int]
          .append(4 :: 5 :: 6 :: Nil)
          .prepend(1 :: 2 :: 3 :: Nil)
          .withFilter(_ % 2 == 0)
          .toList == 2 :: 4 :: 6 :: Nil
      )
      assert(
        DiffList[Int]
          .append(4 :: 5 :: 6 :: Nil)
          .prepend(1 :: 2 :: 3 :: Nil)
          .withFilter(_ % 2 != 0)
          .toList == 1 :: 3 :: 5 :: Nil
      )
      assert(
        DiffList[Int]
          .append(4 :: 5 :: 6 :: Nil)
          .prepend(1 :: 2 :: 3 :: Nil)
          .withFilter(_ > 3)
          .toList == 4 :: 5 :: 6 :: Nil
      )
    }
  }
}
