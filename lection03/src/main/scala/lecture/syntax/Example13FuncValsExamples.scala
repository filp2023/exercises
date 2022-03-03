package lecture.syntax

import scala.util.{Failure, Success, Try}

object Example13FuncValsExamples extends App {
  //MAP api
  Map("foo" -> "bar").map({(kv: Tuple2[String, String]) =>
    kv._1 -> kv._2.length

  })
  Map("foo" -> "bar").map { case (k, v) => k -> v.length }


  //LIST api
  List(1,2,3).flatMap({(x: Int) =>
    List(x, x * 22)
  })
  List(1,2,3).flatMap { x => List(x, x * 22) }


  // TRY and funs
  Try {
    1 / 0
  }.map({(x: Int) =>
    x * 2
  })

  Try {
    1 / 0
  }.map(_ * 2)

}
