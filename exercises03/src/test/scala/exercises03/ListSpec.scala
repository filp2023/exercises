package exercises03

class ListSpec extends org.scalatest.wordspec.AnyWordSpec {
  "MyList.sum" should {
    "sum list" in {
      val list = Cons(4, Cons(3, Cons(2, Cons(1, Nil))))

      assert(MyList.sum(list) == 10)
    }

    "sum Nil" in {
      val list = Nil

      assert(MyList.sum(list) == 0)
    }
  }

  "MyList.reverse" should {
    "reverse list" in {
      val list = Cons(4, Cons(3, Cons(2, Cons(1, Nil))))

      assert(MyList.reverse(list) == Cons(1, Cons(2, Cons(3, Cons(4, Nil)))))
    }

    "reverse Nil" in {
      val list = Nil

      assert(MyList.reverse(list) == Nil)
    }
  }
}
