package service

import cats.effect.{ContextShift, IO}
import org.scalatest.wordspec.AnyWordSpec
import service.domain.GetTweetResponse.{Found, NotFound}
import twitter.domain.{TweetId, User}
import twitter.{LocalTwitterApi, TwitterApi}

import java.util.concurrent.Executors
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext}

class IOTwitterServiceSpec extends AnyWordSpec {
  "IOTwitterService" should {
    "tweet and get" in {
      implicit val ec: ContextShift[IO] =
        IO.contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi             = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO] = new TwitterServiceIO(api)

      val (response, id) = Await.result((for {
        id    <- service.tweet(User("abc"), "abc")
        tweet <- service.getTweet(id)
      } yield (tweet, id)).unsafeToFuture(), 1.second)

      assert(response match {
        case NotFound(_) => false
        case Found(info) => info.id == id
      })
    }

    "tweet two and get two" in {
      implicit val ec: ContextShift[IO] =
        IO.contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi             = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO] = new TwitterServiceIO(api)

      val (response, id, id2) = Await.result(
        (for {
          id    <- service.tweet(User("abc"), "abc")
          id2   <- service.tweet(User("abc"), "abc")
          tweet <- service.getTweets(List(id, id2))
        } yield (tweet, id, id2)).unsafeToFuture(),
        1.second
      )

      assert(response.found.map(_.id) == Set(id, id2))
    }

    "not found" in {
      implicit val ec: ContextShift[IO] =
        IO.contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi             = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO] = new TwitterServiceIO(api)

      val notFoundId = TweetId.generate()
      val response   = Await.result(service.getTweet(notFoundId).unsafeToFuture(), 1.second)

      assert(response match {
        case NotFound(id) => id == notFoundId
        case Found(_)     => false
      })
    }

    "tweet and like" in {
      implicit val ec: ContextShift[IO] =
        IO.contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi             = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO] = new TwitterServiceIO(api)

      val user = User("abc")
      val response = Await.result((for {
        id    <- service.tweet(user, "abc")
        _     <- service.like(user, id)
        tweet <- service.getTweet(id)
      } yield tweet).unsafeToFuture(), 1.second)

      assert(response match {
        case NotFound(_) => false
        case Found(info) => info.likedBy == Set(user)
      })
    }

    "two like idempotent" in {
      implicit val ec: ContextShift[IO] =
        IO.contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi             = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO] = new TwitterServiceIO(api)

      val user = User("abc")
      val response = Await.result((for {
        id    <- service.tweet(user, "abc")
        _     <- service.like(user, id)
        _     <- service.like(user, id)
        tweet <- service.getTweet(id)
      } yield tweet).unsafeToFuture(), 1.second)

      assert(response match {
        case NotFound(_) => false
        case Found(info) => info.likedBy == Set(user)
      })
    }

    "tweet, like, unlike" in {
      implicit val ec: ContextShift[IO] =
        IO.contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi             = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO] = new TwitterServiceIO(api)

      val user = User("abc")
      val (response1, response2) = Await.result(
        (for {
          id           <- service.tweet(user, "abc")
          _            <- service.like(user, id)
          likedTweet   <- service.getTweet(id)
          _            <- service.unlike(user, id)
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

    "two unlike idempotent" in {
      implicit val ec: ContextShift[IO] =
        IO.contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1)))

      val api: TwitterApi             = new LocalTwitterApi(Iterator.continually(0))
      val service: TwitterService[IO] = new TwitterServiceIO(api)

      val user = User("abc")
      val (response1, response2) = Await.result(
        (for {
          id           <- service.tweet(user, "abc")
          _            <- service.like(user, id)
          likedTweet   <- service.getTweet(id)
          _            <- service.unlike(user, id)
          _            <- service.unlike(user, id)
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
  }
}
