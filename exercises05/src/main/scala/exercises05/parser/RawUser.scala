package exercises05.parser

case class RawUser(
    id: String,
    banned: Boolean,
    passport: Option[String],
    firstName: Option[String],
    secondName: Option[String],
    thirdName: Option[String]
)
