// паттерн матчинг

trait Pet

class Cat extends Pet {
  def meow(): String = "meow-meow"
}

class Dog extends Pet {
  def bark(): String = "woof-woof"
}

val pet: Pet = new Cat

pet match {
  case cat: Cat => cat.meow()
  case dog: Dog => dog.bark()
}