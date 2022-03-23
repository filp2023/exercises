package examples

trait Show[A] {
  def show(value: A): String
}

object ToStringExample extends App {

  class Group(name: String)

  case class User(name: String, group: Group)

  println(User("John", new Group("root")).toString)
  // prints User(John,examples.ToStringExample$Group@3f3afe78)
}

object ShowInstances {

  implicit val showInt: Show[Int] = new Show[Int] {
    def show(value: Int): String = value.toString
  }

  implicit val showString: Show[String] = new Show[String] {
    def show(value: String): String = value
  }
}

object ShowSyntax {

  implicit class ShowOps[A](private val a: A) extends AnyVal {
    def show(implicit ev: Show[A]): String = ev.show(a)
  }
}

object ShowExample extends App {

  import ShowInstances._
  import ShowSyntax._

  class User(val name: String, val age: Int)

  object User {

    implicit val showUser: Show[User] = new Show[User] {
      def show(user: User): String = s"User(name=${user.name.show} age=${user.age.show})"
    }
  }

  import ShowSyntax._
  import User._

  println(new User("John", 30).show)


  class Money(val amount: Double, val currency: String)

  object Money {
    import ShowInstances._
    import ShowSyntax._

//    implicit val showMoney: Show[Money] = new Show[Money] {
//      def show(money: Money): String = s"Money(amount=${money.amount.show} currency=${money.currency.show})"
//    }
  }
}
