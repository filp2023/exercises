package competition

import cats.effect.{ContextShift, IO}
import cats.effect.IO.contextShift
import org.scalatest.wordspec.AnyWordSpec
import service.domain.GetTweetResponse.{Found, NotFound}
import service.{TwitterService, TwitterServiceIO}
import twitter.domain.User
import twitter.{LocalTwitterApi, TwitterApi}

import java.util.concurrent.Executors
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}

class CompetitionMethodsSpec extends AnyWordSpec {
  "CompetitionMethods" should {
    "unlikeAll one tweet" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi                 = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val user = User("abc")
      val (response1, response2) = Await.result(
        (for {
          id           <- service.tweet(user, "abc")
          _            <- service.like(user, id)
          likedTweet   <- service.getTweet(id)
          _            <- methods.unlikeAll(user, List(id))
          unlikedTweet <- service.getTweet(id)
        } yield (likedTweet, unlikedTweet)).unsafeToFuture(),
        1.second
      )

      assert(response1 match {
        case NotFound(_) => false
        case Found(info) => info.likedBy == Set(user)
      })
      assert(response2 match {
        case NotFound(_) => false
        case Found(info) => info.likedBy.isEmpty
      })
    }

    "unlikeAll tweets" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi                 = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val user = User("abc")
      val (response1, response2) = Await.result(
        (for {
          id            <- service.tweet(user, "abc")
          id2           <- service.tweet(user, "abc2")
          _             <- service.like(user, id)
          _             <- service.like(user, id2)
          _             <- methods.unlikeAll(user, List(id, id2))
          unlikedTweet  <- service.getTweet(id)
          unlikedTweet2 <- service.getTweet(id2)
        } yield (unlikedTweet, unlikedTweet2)).unsafeToFuture(),
        1.second
      )

      assert(response1 match {
        case NotFound(_) => false
        case Found(info) => info.likedBy.isEmpty
      })
      assert(response2 match {
        case NotFound(_) => false
        case Found(info) => info.likedBy.isEmpty
      })
    }

    "unlikeAll no tweets" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi                 = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val user = User("abc")
      val (response1, response2) = Await.result(
        (for {
          id           <- service.tweet(user, "abc")
          _            <- service.like(user, id)
          likedTweet   <- service.getTweet(id)
          _            <- methods.unlikeAll(user, List())
          unlikedTweet <- service.getTweet(id)
        } yield (likedTweet, unlikedTweet)).unsafeToFuture(),
        1.second
      )

      assert(response1 == response2)
    }

    "topAuthor by likes" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi                 = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val user  = User("abc")
      val user2 = User("abc2")
      val topAuthor = Await.result(
        (for {
          id        <- service.tweet(user, "abc")
          id2       <- service.tweet(user2, "abc2")
          _         <- service.like(user, id)
          _         <- service.like(user, id2)
          _         <- service.like(user2, id2)
          topAuthor <- methods.topAuthor(List(id, id2))
        } yield topAuthor).unsafeToFuture(),
        1.second
      )

      assert(topAuthor.contains(user2))
    }

    "topAuthor by time" in {
      implicit val cs: ContextShift[IO] = contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi                 = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO]     = new TwitterServiceIO(api)
      val methods: CompetitionMethods[IO] = new CompetitionMethods[IO](service)

      val user  = User("abc")
      val user2 = User("abc2")
      val topAuthor = Await.result(
        (for {
          id        <- service.tweet(user, "abc")
          id2       <- service.tweet(user2, "abc2")
          _         <- service.like(user, id)
          _         <- service.like(user2, id2)
          topAuthor <- methods.topAuthor(List(id, id2))
        } yield topAuthor).unsafeToFuture(),
        1.second
      )

      assert(topAuthor.contains(user))
    }
  }
}
