package adt

import scala.annotation.tailrec

// Модификатор sealed - наследование возможно только в рамках текущего файла
object example1 extends App {
  sealed trait Pet

  case class Dog(id: Long, name: String) extends Pet

  case class Cat(id: Long, name: String, color: String) extends Pet

  val dog = new Dog(1, "Doggie")
  val cat = Cat(2, "Cattie", "Black")

  val dog2 = Dog.apply(1, "Doggie")
  val cat2 = Cat.apply(2, "Cattie", "Black")

  println(dog.id) // 1

  println(dog) // Dog(1,Doggie)

  println(cat) // Cat(2,Cattie,Black)

  println(cat == cat2) // true

  val pets: List[Pet] = List(dog, cat)

  println(pets) // List(Dog(1,Doggie), Cat(2,Cattie,Black))
}

// Ключевое слово object создает singleton-class
object example2 extends App {
  sealed trait Pet

  sealed trait Color
  object Color {
    case object Red extends Color
    case object Black extends Color
    case object White extends Color
  }

  case class Dog(id: Long, name: String) extends Pet

  case class Cat(id: Long, name: String, color: Color) extends Pet

  val dog = Dog(1, "Doggie")
  val cat = Cat(2, "Cattie", Color.Black)

  // Определенный компилятором метод copy
  val whiteCat = cat.copy(id = 3, color = Color.White)

  // Exhaustive pattern matching
  def color(pet: Pet): Color =
    pet match {
      case Cat(_, _, color) => color
      case _: Dog           => Color.Red
    }

  def showColor(color: Color): String =
    color match {
      case Color.Red   => "RED"
      case Color.Black => "BLACK"
      case Color.White => "WHITE"
    }

  println(color(dog)) // Red

  println(color(cat)) // Black

  println(color(whiteCat)) // White

  println(showColor(Color.Black)) // BLACK
}

object example3 extends App {
  // val у поля класса делает поле публичным
  class Dog(val id: Int, val name: String) {

    override def toString: String = s"Dog($id, $name)"

    override def equals(obj: Any): Boolean =
      if (obj == this) true
      else if (obj != null && obj.getClass == this.getClass) {
        val other = obj.asInstanceOf[Dog]

        other.id == id && other.name == this.name
      } else false

    override def hashCode(): Int = java.util.Objects.hash(id, name)
  }

  object Dog {
    // Создание класса
    def apply(id: Int, name: String): Dog =
      new Dog(id, name)

    // Набор полей класса
    def unapply(dog: Dog): Option[(Int, String)] =
      Some((dog.id, dog.name))
  }

  val dog = Dog(1, "Doggie")

  println(dog.name) // Doggie

  println(dog == Dog(1, "Doogie")) // true

  def doogie(dog: Dog): Long =
    dog match {
      case Dog(id, "Doggie") => id
      case Dog(id, _)        => id + 1
    }

  println(doogie(dog)) // 1
}

object examples4 extends App {
  sealed trait Tree[+A]

  case class Node[+A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]

  case object Leaf extends Tree[Nothing]

  val intTree: Tree[Int] = Node(
    value = 2,
    left = Node(3, Node(1, Leaf, Leaf), Leaf),
    right = Node(4, Leaf, Leaf)
  )

  def sum(tree: Tree[Int]): Int =
    tree match {
      case Node(value, left, right) =>
        value + sum(left) + sum(right)
      case Leaf => 0
    }

  println(sum(intTree)) // 10
}

object examples5 extends App {
  // Tuples

  val a: (String, Int) = ("str", 1)

  val a2: Tuple2[String, Int] = Tuple2("str", 1)

  println(a == a2) // true

  println(a._1) // "str"
  print(a._2) // 1

  println(a match { case (first, second) => s"$first, $second" })

  // Option - наличие значения Some или отсутсвие значения None
  sealed trait Option[+A]

  final case class Some[+A](value: A) extends Option[A]

  case object None extends Option[Nothing]

  val some: Option[Int] = Some(1)

  val none: Option[Int] = None

  // Either - либо ветка со значением A, либо ветка со значением B
  sealed trait Either[+A, +B]

  final case class Left[+A, +B](value: A) extends Either[A, B]

  final case class Right[+A, +B](value: B) extends Either[A, B]

  val right: Either[String, Int] = Right(1)

  val left: Either[String, Int] = Left("error")

  // List - либо констуктор односвязанного списка, либо пустой список Nil

  sealed trait List[+A] {
    def ::[B >: A](elem: B): List[B] = new ::(elem, this)
  }

  final case class ::[+A](head: A, tail: List[A]) extends List[A]

  case object Nil extends List[Nothing]

  val list: List[Int] = 1 :: 2 :: 3 :: Nil

  def sum(list: List[Int]): Int = {
    @scala.annotation.tailrec
    def loop(list: List[Int], acc: Int): Int = {
      list match {
        case head :: tail => loop(tail, acc + head)
        case Nil          => acc
      }
    }
    loop(list, 0)
  }

  println(sum(list)) // 6
}

object examples6 extends App {
  sealed trait MakeGroupsResult
  object MakeGroupsResult {
    case object NoParticipants extends MakeGroupsResult
    case class SingleParticipant(login: String) extends MakeGroupsResult
    case class Success(groups: List[Group]) extends MakeGroupsResult
  }

  sealed trait Group
  object Group {
    case class TwoPerson(first: String, second: String) extends Group
    case class ThirdPerson(first: String, second: String, third: String) extends Group
  }

  def makeGroups(participants: List[String]): MakeGroupsResult = {
    @tailrec
    def takeGroups(first: String, second: String, restParticipants: List[String], acc: List[Group]): List[Group] =
      restParticipants match {
        case Nil           => Group.TwoPerson(first, second) :: acc
        case single :: Nil => Group.ThirdPerson(first, second, single) :: acc
        case nextFirst :: nextSecond :: rest =>
          takeGroups(nextFirst, nextSecond, rest, Group.TwoPerson(first, second) :: acc)
      }

    participants match {
      case Nil                     => MakeGroupsResult.NoParticipants
      case single :: Nil           => MakeGroupsResult.SingleParticipant(single)
      case first :: second :: tail => MakeGroupsResult.Success(takeGroups(first, second, tail, Nil))
    }
  }

  println(makeGroups(Nil))

  println(makeGroups(List("ilya")))

  println(makeGroups(List("ilya", "ivan", "igor")))

  println(makeGroups(List("ilya", "ivan", "igor", "maria")))
}