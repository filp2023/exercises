package part1

object Example01_Subtyping {

  trait Vehicle {
    def drive(destination: String): Unit
  }

  class Car extends Vehicle {
    private var fuel: Int = 0

    override def drive(destination: String): Unit = ???

    def fillFuelTank(): Unit = ???
  }

  trait CargoContainer {
    def load(cargo: String): Unit
    def unload(): String
  }

  class PickupTruck extends Car with CargoContainer {
    private var cargo: Option[String] = None

    override def load(cargo: String): Unit = ???

    override def unload(): String = ???
  }

}
