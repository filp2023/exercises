package fintech.lecture.examples

object Example12Vector {
  // construct
  val vec1 = Vector(1,2,3,4,5)
  val vec2 = Vector.tabulate(5)(x => x * x)

  //access
  val head = vec1.head // not good
  val tail = vec1.tail
  val headOpt = vec1.headOption
  val elem = vec1(2)

  // with pattern matching
  vec2 match {
    case tail +: head => s"$head and $tail"
    case _            => "this is the end"
  }

  //update above and add element
  val vec3  = 0 +: vec2
  val vec4 = vec2 :+ 0
  val vec5 = vec2.updated(1,42)
}
