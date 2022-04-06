package monad

import Monoid.Syntax._
import Monad.Syntax._

object Example05WriterMonad extends App {
  final case class Writer[Log, Value](run: (Log, Value)) {
    def tell(nextLog: Log)(implicit monoid: Monoid[Log]): Writer[Log, Value] =
      run match { case (log, value) => Writer((log |+| nextLog, value)) }
  }

  object Writer {
    def tell[Log](log: Log): Writer[Log, Unit] =
      Writer((log, ()))

    implicit def monad[Log: Monoid]: Monad[Writer[Log, *]] = new Monad[Writer[Log, *]] {
      def flatMap[A, B](fa: Writer[Log, A])(f: A => Writer[Log, B]): Writer[Log, B] =
        fa.run match {
          case (log, value) =>
            f(value).run match { case (nextLog, nextValue) => Writer((log |+| nextLog, nextValue)) }
        }

      def pure[A](a: A): Writer[Log, A] =
        Writer((Monoid[Log].empty, a))
    }
  }

  case class Logs(list: List[String])

  object Logs {
    implicit val monoid: Monoid[Logs] = new Monoid[Logs] {
      def combine(x: Logs, y: Logs): Logs =
        Logs(x.list ::: y.list)

      def empty: Logs =
        Logs(Nil)
    }

    def single(string: String): Logs =
      Logs(List(string))
  }

  case class User(name: Option[String], surname: Option[String])

  object ClassicService {
    def loadUserName(id: Long): (Logs, Option[String]) =
      id match {
        case 1 => (Monoid[Logs].empty, Option("Alice"))
        case 2 => (Monoid[Logs].empty, Option("Bob"))
        case _ => (Logs.single("Can't find name"), None)
      }

    def loadUserSurname(id: Long): (Logs, Option[String]) =
      id match {
        case 1 => (Logs.single("Surname is Smith"), Option("Smith"))
        case 2 => (Logs.single("Surname is Johnson"), Option("Johnson"))
        case _ => (Logs.single("Can' find surname"), None)
      }

    def loadUser(id: Long): (Logs, User) = {
      (loadUserName(id), loadUserSurname(id)) match {
        case ((nameLogs, name), (surnameLogs, surname)) =>
          (
            Logs.single("Loading user name") |+| nameLogs |+| Logs.single("Loading user surname") |+| surnameLogs,
            User(name, surname)
          )
      }
    }
  }

  object WriterService {
    type WithLogs[A] = Writer[Logs, A]
    object WithLogs {
      def log(str: String): WithLogs[Unit] =
        Writer((Logs.single(str), ()))
    }

    def loadUserName(id: Long): WithLogs[Option[String]] =
      id match {
        case 1 => Option("Alice").pure[WithLogs]
        case 2 => Option("Bob").pure[WithLogs]
        case _ => WithLogs.log("Can't find name").map(_ => None)
      }

    def loadUserSurname(id: Long): WithLogs[Option[String]] =
      id match {
        case 1 =>
          Option("Smith").pure[WithLogs].tell(Logs.single("Surname is Smith"))
        case 2 =>
          Option("Johnson").pure[WithLogs].tell(Logs.single("Surname is Johnson"))
        case _ => WithLogs.log("Can't find surname").map(_ => None)
      }

    def loadUser(id: Long): WithLogs[User] =
      for {
        _       <- WithLogs.log("Loading user name")
        name    <- loadUserName(id)
        _       <- WithLogs.log("Loading user surname")
        surname <- loadUserSurname(id)
      } yield User(name, surname)
  }


  println(ClassicService.loadUser(1))
  println(ClassicService.loadUser(2))
  println(ClassicService.loadUser(3))

  println()

  println(WriterService.loadUser(1).run)
  println(WriterService.loadUser(2).run)
  println(WriterService.loadUser(3).run)
}
