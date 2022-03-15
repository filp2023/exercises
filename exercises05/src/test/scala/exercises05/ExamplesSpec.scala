package exercises05

import exercises05.either.EitherCombinators._
import exercises05.parser.Error._
import exercises05.parser.Examples._
import exercises05.parser._
import org.scalatest.wordspec.AnyWordSpec

class ExamplesSpec extends AnyWordSpec {
  "Examples.transformToOption" should {
    "если rawUser.firstName или rawUser.secondName == None, то функция должна вернуть None" in {
      assert(
        transformToOption(RawUser(Long.MaxValue.toString, banned = false, None, None, Some("Odersky"), None)).isEmpty
      )
      assert(
        transformToOption(RawUser(Long.MaxValue.toString, banned = false, None, Some("Martin"), None, None)).isEmpty
      )
      assert(transformToOption(RawUser(Long.MaxValue.toString, banned = false, None, None, None, None)).isEmpty)
    }

    "если rawUser.passport == None или rawUser.thirdName == None, то и в результирующем User эти поля == None" in {
      assert(
        transformToOption(RawUser(Long.MaxValue.toString, banned = false, None, Some("Martin"), Some("Odersky"), None))
          .contains(User(Long.MaxValue, UserName("Martin", "Odersky", None), None))
      )
    }

    "passport должен быть передан в формате 1234 567890, если не так, то функция должна вернуть None" in {
      assert(
        transformToOption(
          RawUser(Long.MaxValue.toString, banned = false, Some("incorrect"), Some("Martin"), Some("Odersky"), None)
        ).isEmpty
      )
    }

    "если rawUser.id не парсится в Long то функция должна вернуть None" in {
      assert(
        transformToOption(
          RawUser("Long.MaxValue.toString", banned = false, None, Some("Martin"), Some("Odersky"), None)
        ).isEmpty
      )
    }

    "если rawUser.banned, то вернуть None" in {
      assert(
        transformToOption(RawUser(Long.MaxValue.toString, banned = true, None, Some("Martin"), Some("Odersky"), None)).isEmpty
      )
    }

    "корректно трансформировать" in {
      assert(
        transformToOption(
          RawUser(Long.MaxValue.toString, banned = false, Some("1234 567890"), Some("Martin"), Some("Odersky"), None)
        ).contains(User(Long.MaxValue, UserName("Martin", "Odersky", None), Some(Passport(1234, 567890))))
      )

      assert(
        transformToOption(
          RawUser(
            Long.MaxValue.toString,
            banned = false,
            Some("1234 567890"),
            Some("Martin"),
            Some("Odersky"),
            Some("")
          )
        ).contains(User(Long.MaxValue, UserName("Martin", "Odersky", Some("")), Some(Passport(1234, 567890))))
      )
    }
  }

  "Examples.transformToEither" should {
    "return right" in {
      assert(
        transformToEither(
          RawUser(Long.MaxValue.toString, banned = false, Some("1234 567890"), Some("Martin"), Some("Odersky"), None)
        )
          == Right(User(Long.MaxValue, UserName("Martin", "Odersky", None), Some(Passport(1234, 567890))))
      )

      assert(
        transformToEither(
          RawUser(
            Long.MaxValue.toString,
            banned = false,
            Some("1234 567890"),
            Some("Martin"),
            Some("Odersky"),
            Some("")
          )
        )
          == Right(User(Long.MaxValue, UserName("Martin", "Odersky", Some("")), Some(Passport(1234, 567890))))
      )

      assert(
        transformToEither(
          RawUser(Long.MaxValue.toString, banned = false, None, Some("Martin"), Some("Odersky"), Some(""))
        )
          == Right(User(Long.MaxValue, UserName("Martin", "Odersky", Some("")), None))
      )
    }

    "return errors in Left with correct priority" in {
      assert(transformToEither(RawUser("abc", banned = true, Some("incorrect"), None, None, None)) == Left(Banned))
      assert(
        transformToEither(RawUser("Long.MaxValue.toString", banned = false, Some("incorrect"), None, None, None)) == Left(
          InvalidId
        )
      )
      assert(
        transformToEither(RawUser(Long.MaxValue.toString, banned = false, Some("incorrect"), None, None, None)) == Left(
          InvalidName
        )
      )
      assert(
        transformToEither(
          RawUser(Long.MaxValue.toString, banned = false, Some("incorrect"), Some("Martin"), None, None)
        ) == Left(InvalidName)
      )
      assert(
        transformToEither(
          RawUser(Long.MaxValue.toString, banned = false, Some("incorrect"), None, Some("Odersky"), None)
        ) == Left(InvalidName)
      )
      assert(
        transformToEither(
          RawUser(Long.MaxValue.toString, banned = false, Some("incorrect"), Some("Martin"), Some("Odersky"), None)
        ) == Left(InvalidPassport)
      )
    }
  }
}
