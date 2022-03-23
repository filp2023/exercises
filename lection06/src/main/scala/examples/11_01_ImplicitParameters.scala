package examples

object Example06ImplicitParameters1 {

  def insertionSort(xs: List[Int]): List[Int] = {
    def insert(y: Int, ys: List[Int]): List[Int] =
      ys match {
        case List() => y :: Nil
        case z :: zs =>
          if (y < z) y :: z :: zs
          else z :: insert(y, zs)
      }

    xs match {
      case List()  => Nil
      case y :: ys => insert(y, insertionSort(ys))
    }
  }
}

object Example06ImplicitParameters2 {
  def insertionSort[A](xs: List[A]): List[A] = ???
}

object Example06ImplicitParameters3 {
  def insertionSort[A](xs: List[A])(lessThan: (A, A) => Boolean): List[A] = {
    def insert(y: A, ys: List[A]): List[A] =
      ys match {
        case List() => y :: Nil
        case z :: zs =>
          if (lessThan(y, z)) y :: z :: zs
          else z :: insert(y, zs)
      }

    xs match {
      case List()  => Nil
      case y :: ys => insert(y, insertionSort(ys)(lessThan))
    }
  }

  val nums = List(-5, 6, 3, 2, 7)
  val fruits = List("apple", "pear", "orange", "pineapple")

  insertionSort(nums)((x: Int, y: Int) => x < y)
  insertionSort(fruits)((x: String, y: String) => x.compareTo(y) < 0)

  insertionSort(nums)((x, y) => x < y)
  insertionSort(fruits)((x, y) => x < y)
}

object Example06ImplicitParameters4 {

  trait Order[A] {
    def lt(a: A, b: A): Boolean
  }

  def insertionSort[A](xs: List[A])(ord: Order[A]): List[A] = {
    def insert(y: A, ys: List[A]): List[A] =
      ys match {
        case List() => y :: Nil
        case z :: zs =>
          if (ord.lt(y, z)) y :: z :: zs
          else z :: insert(y, zs)
      }

    xs match {
      case List()  => Nil
      case y :: ys => insert(y, insertionSort(ys)(ord))
    }
  }

  val intOrder: Order[Int] = (a, b) => a.compareTo(b) < 0

  val stringOrder: Order[String] = (a, b) => a.compareTo(b) < 0

  val nums = List(-5, 6, 3, 2, 7)
  val fruits = List("apple", "pear", "orange", "pineapple")

  insertionSort(nums)(intOrder)
  insertionSort(fruits)(stringOrder)
}

object Example06ImplicitParameters5 extends App{
  trait Order[A] {
    def lt(a: A, b: A): Boolean
  }

  def insertionSort[A](xs: List[A])(implicit ord: Order[A]): List[A] = {
    def insert(y: A, ys: List[A]): List[A] =
      ys match {
        case List() => y :: Nil
        case z :: zs =>
          if (ord.lt(y, z)) y :: z :: zs
          else z :: insert(y, zs)
      }

    xs match {
      case List()  => Nil
      case y :: ys => insert(y, insertionSort(ys)(ord))
    }
  }

  implicit val intOrder: Order[Int] = (a, b) => a.compareTo(b) < 0

  implicit val stringOrder: Order[String] = (a, b) => a.compareTo(b) < 0

  val nums = List(-5, 6, 3, 2, 7)
  val fruits = List("apple", "pear", "orange", "pineapple")

  insertionSort(nums)
  insertionSort(fruits)
}
