package service

import cats.effect.IO
import cats.syntax.all._

import service.domain._

import twitter.domain.TwitterError._
import twitter.TwitterApi
import twitter.domain._

// Воспользуйтесь синтаксисом map, recover, traverse из cats.syntax.all_
class TwitterServiceIO(api: TwitterApi) extends TwitterService[IO] {
  def tweet(user: User, text: String): IO[TweetId] = ???

  def like(user: User, tweetId: TweetId): IO[Unit] = ???

  def unlike(user: User, tweetId: TweetId): IO[Unit] = ???

  def getTweet(tweetId: TweetId): IO[GetTweetResponse] = ???

  def getTweets(ids: List[TweetId]): IO[GetTweetsResponse] = ???
}
