package competition

import cats.effect.IO.contextShift
import cats.effect.{ContextShift, IO}
import competition.domain.ScenarioError.TopAuthorNotFound
import org.scalatest.wordspec.AnyWordSpec
import service.{TwitterService, TwitterServiceIO}
import twitter.domain.User
import twitter.{LocalTwitterApi, TwitterApi}

import java.util.concurrent.Executors
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}

class IOCompetitionSpec extends AnyWordSpec {
  val oleg: User = User("oleg")
  val ivan: User = User("ivan")
  val bot: User  = User("bot")

  val users = List(oleg, ivan, bot)

  "IOCompetition" should {
    "empty competition" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2)))

      val api: TwitterApi                 = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val followers: Map[User, List[User]] = Map(
        oleg -> List(ivan),
        ivan -> List(oleg),
        bot  -> Nil
      )

      assert(
        Await
          .result(
            new IOCompetition(service, methods)
              .winner(Nil, followers, bot)
              .map(Some(_))
              .handleErrorWith { case TopAuthorNotFound => IO.pure(None) }
              .unsafeToFuture(),
            1.second
          )
          .isEmpty
      )
    }

    "async competition oleg win" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2)))

      val api: TwitterApi                 = new LocalTwitterApi(Iterator.continually(0), Map(oleg -> 0, ivan -> 100))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val followers: Map[User, List[User]] = Map(
        oleg -> List(ivan),
        ivan -> List(oleg),
        bot  -> Nil
      )

      assert(
        Await
          .result(new IOCompetition(service, methods).winner(users, followers, bot).unsafeToFuture(), 1.second) == oleg
      )
    }

    "async competition ivan win first tweet" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2)))

      val api: TwitterApi =
        new LocalTwitterApi(Iterator.continually(10), Map(ivan -> 0))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val followers: Map[User, List[User]] = Map(
        oleg -> List(ivan),
        ivan -> List(oleg),
        bot  -> Nil
      )

      assert(
        Await
          .result(new IOCompetition(service, methods).winner(users, followers, bot).unsafeToFuture(), 1.seconds) == ivan
      )
    }

    "sync competition oleg win" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi =
        new LocalTwitterApi(Iterator.continually(0), Map(oleg -> 100))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val followers: Map[User, List[User]] = Map(
        oleg -> List(ivan),
        ivan -> List(oleg),
        bot  -> Nil
      )

      assert(
        Await
          .result(new IOCompetition(service, methods).winner(users, followers, bot).unsafeToFuture(), 1.second) == oleg
      )
    }

    "async competition ivan win max likes" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2)))

      val api: TwitterApi =
        new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val followers: Map[User, List[User]] = Map(
        oleg -> List(ivan),
        ivan -> List(oleg, ivan),
        bot  -> Nil
      )

      assert(
        Await
          .result(new IOCompetition(service, methods).winner(users, followers, bot).unsafeToFuture(), 1.seconds) == ivan
      )
    }

    "async competition ivan win bot removed" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2)))

      val api: TwitterApi =
        new LocalTwitterApi(Iterator.continually(0), Map(oleg -> 100))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val followers: Map[User, List[User]] = Map(
        oleg -> List(ivan, oleg, bot),
        ivan -> List(oleg, ivan),
        bot  -> Nil
      )

      assert(
        Await
          .result(new IOCompetition(service, methods).winner(users, followers, bot).unsafeToFuture(), 1.second) == ivan
      )
    }
  }
}
