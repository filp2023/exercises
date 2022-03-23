package examples

case class Rational(numerator: Int, denumerator: Int) {

  def +(that: Rational): Rational =
    Rational(this.numerator * that.denumerator + that.numerator * this.denumerator, this.denumerator * that.denumerator)

  def +(i: Int): Rational = this + Rational(i, 1)

}

object Example05Usage2a {

  val half: Rational = Rational(1, 2)

  half + half

  half + 1

//  1 + half

  object Implicit {
    implicit def int2rational(i: Int): Rational = Rational(i, 1)

    1 + half // replaced by int2rational(1) + half
  }

}
