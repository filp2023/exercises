package fintech.lecture.examples

object Example13Map {
  // construct
  val map = Map("one" -> 1, "two" -> 2, "three" -> 3)

  //access
  val elem = map("one") // map is like function // this method is unsafe
  val elemOpt = map.get("one") // this one is safe

  //updates
  val map1 = map + ("four" -> 4)
  val map2 = map - "one"
}
