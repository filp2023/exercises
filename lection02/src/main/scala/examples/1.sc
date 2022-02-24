// строгая типизация

val question: String = "question of life"

val answer = 42

//val answer: String = 42
/*
type mismatch;
found   : Int(42)
required: String
val answer: String = 42
 */

val e: Double = 2

//val e: Int = 2.72
/*
type mismatch;
found   : Double(2.72)
required: Int
*/

trait Pet
class Cat extends Pet {
  def meow(): String = "meow-meow"
}
class Dog extends Pet {
  def bark(): String = "woof-woof"
}

val pet: Pet = new Cat
//pet.meow // value meow is not a member of Pet
pet match {
  case cat: Cat => cat.meow()
  case dog: Dog => dog.bark()
}

trait Pet {
  type Voice
  def voice: Voice
}
class Cat extends Pet {
  type Voice = String
  def voice = "meow-meow"
}
class Robot extends Pet {
  type Voice = Int
  def voice = 31337
}

new Robot().voice
