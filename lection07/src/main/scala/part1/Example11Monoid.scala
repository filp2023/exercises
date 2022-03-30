package part1

object Example11Monoid extends App {
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  object Semigroup {
    def apply[A](implicit inst: Semigroup[A]): Semigroup[A] = inst
  }

  object SemigroupSyntax {
    implicit class SemigroupOps[A](private val a: A) extends AnyVal {
      def |+|(b: A)(implicit S: Semigroup[A]): A =
        S.combine(a, b)
    }
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit inst: Monoid[A]): Monoid[A] = inst
  }

  // Законы
  // (x |+| y) |+| z = x |+| (y |+| z)  -- associativity
  // empty |+| x = x                    -- left identity
  // x |+| empty = x                    -- right identity


  // Пример, Int, сложение

  implicit val intSumMonoid: Monoid[Int] = new Monoid[Int] {
    override def empty: Int = 0
    override def combine(x: Int, y: Int): Int = x + y
  }

  // Пример, инвентарь из игры
  //

  type Inventory = Map[String, Int]

  implicit val inventoryMonoid: Monoid[Inventory] = new Monoid[Inventory] {
    def empty: Inventory = Map.empty

    def combine(x: Inventory, y: Inventory): Inventory = {
      val keys = x.keySet ++ y.keySet

      keys.map { key =>
        val newValue = x.getOrElse(key, 0) + y.getOrElse(key, 0)

        key -> newValue
      }.toMap
    }
  }


  import SemigroupSyntax._

  val myInventory = Map(
    "gold" -> 10,
    "wood" -> 20,
    "rock" -> 30
  )

  val loot = Map(
    "wood" -> 3
  )

  val loot2 = Map(
    "rock" -> 13
  )

  myInventory |+| loot |+| loot2


  // Пример, абстрактный код

  def combineAll[A: Monoid](list: List[A]): A =
    list.fold(Monoid[A].empty)(_ |+| _)

  combineAll(List(1, 2, 3))
  combineAll(List(myInventory, loot, loot2))

}
