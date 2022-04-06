package monad

import Monad.Syntax._

object Example06StateMonad extends App {
  final case class State[S, A](run: S => (A, S))

  object State {
    def ask[S, StateInfo](fn: S => StateInfo): State[S, StateInfo] =
      State(s => (fn(s), s))

    def update[S](fn: S => S): State[S, Unit] =
      State(s => ((), fn(s)))

    implicit def monad[S]: Monad[State[S, *]] = new Monad[State[S, *]] {
      def flatMap[A, B](fa: State[S, A])(f: A => State[S, B]): State[S, B] =
        State(s => fa.run(s) match { case (value, nextState) => f(value).run(nextState) })

      def pure[A](a: A): State[S, A] =
        State(s => (a, s))
    }
  }

  case class Pocket(cash: Double, goods: Set[Good])

  case class Good(name: String, price: Double)

  trait Shop {
    def find(fn: Good => Boolean): Option[Good]

    def remove(good: Good): Shop
  }

  case class Status(pocket: Pocket, shop: Shop)

  object ClassicService {
    def buyGood(predicate: Good => Boolean, status: Status): (Status, Option[Good]) = {
      val pocket = status.pocket
      val shop   = status.shop

      val maybeGood = shop.find(predicate)

      val newStatus = maybeGood match {
        case Some(good) =>
          Status(pocket = Pocket(pocket.cash - good.price, goods = pocket.goods + good), shop.remove(good))
        case None => status
      }

      (newStatus, maybeGood)
    }

    def buyCoffee(status: Status): (Status, Option[Good]) =
      buyGood(good => good.name == "Кофе" && good.price <= status.pocket.cash, status)

    def buyTea(status: Status): (Status, Option[Good]) =
      buyGood(good => good.name == "Чай" && good.price <= status.pocket.cash, status)

    def buySugar(status: Status): (Status, Option[Good]) =
      buyGood(good => good.name == "Сахар" && good.price <= 10 && good.price <= status.pocket.cash, status)

    def buyAll(status: Status): (Status, List[Good]) = {
      val (status1, maybeCoffee) = buyCoffee(status)
      val (status2, maybeTea)    = buyTea(status1)
      val (status3, maybeSugar)  = buySugar(status2)

      (status3, List(maybeCoffee, maybeTea, maybeSugar).flatten)
    }
  }

  object StateService {
    type WithStatus[A] = State[Status, A]
    object WithStatus {
      def pocket: WithStatus[Pocket] =
        State.ask[Status, Pocket](_.pocket)

      def shop: WithStatus[Shop] =
        State.ask[Status, Shop](_.shop)

      def setPocket(pocket: Pocket): WithStatus[Unit] =
        State.update(_.copy(pocket = pocket))

      def setShop(shop: Shop): WithStatus[Unit] =
        State.update(_.copy(shop = shop))
    }

    def buyGood(predicate: Good => Boolean): WithStatus[Option[Good]] =
      for {
        shop   <- WithStatus.shop
        pocket <- WithStatus.pocket
        maybeGood = shop.find(predicate)
        _ <- maybeGood match {
          case Some(good) =>
            for {
              _ <- WithStatus.setPocket(pocket.copy(cash = pocket.cash - good.price, goods = pocket.goods + good))
              _ <- WithStatus.setShop(shop.remove(good))
            } yield ()
          case None =>
            ().pure[WithStatus]
        }
      } yield maybeGood

    def buyCoffee: WithStatus[Option[Good]] =
      for {
        pocket <- WithStatus.pocket
        good   <- buyGood(good => good.name == "Кофе" && good.price <= pocket.cash)
      } yield good

    def buyTea: WithStatus[Option[Good]] =
      for {
        pocket <- WithStatus.pocket
        good   <- buyGood(good => good.name == "Чай" && good.price <= pocket.cash)
      } yield good

    def buySugar: WithStatus[Option[Good]] =
      for {
        pocket <- WithStatus.pocket
        good   <- buyGood(good => good.name == "Сахар" && good.price <= 10 && good.price <= pocket.cash)
      } yield good

    def buyAll: WithStatus[List[Good]] =
      for {
        maybeCoffee <- buyCoffee
        maybeTea    <- buyTea
        maybeSugar  <- buySugar
      } yield List(maybeCoffee, maybeTea, maybeSugar).flatten
  }

  class SimpleShop(goods: List[Good]) extends Shop {
    def find(fn: Good => Boolean): Option[Good] =
      goods.find(fn)

    def remove(good: Good): Shop =
      new SimpleShop(goods.filterNot(_ == good))

    override def toString: String =
      s"Shop(${goods.mkString(",")})"
  }

  val startShop: Shop = new SimpleShop(
    List(Good("Кофе", 100.0), Good("Чай", 200.0), Good("Сахар", 50.0), Good("Сахар", 8.50))
  )
  val startPocket: Pocket = Pocket(cash = 360.0, goods = Set.empty)

  println("Classic:")

  val (status, goods) = ClassicService.buyAll(Status(startPocket, startShop))

  println(s"Status: $status")
  println(s"Bought goods: $goods")

  println()

  println("State:")


  val (goodsS, statusS) = StateService.buyAll.run(Status(startPocket, startShop))

  println(s"Status: $statusS")
  println(s"Bought goods: $goodsS")
}
