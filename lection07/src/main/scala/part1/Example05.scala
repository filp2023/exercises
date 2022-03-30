package part1

object Example05 {
  // Тот же код с TC

  object VehicleSimulationLibrary {
    class PickupTruck {
      var fuel: Int = 0
      var cargo: Option[String] = None

      // ???
    }

    def createPickupTrucks: List[PickupTruck] =
      List(new PickupTruck, new PickupTruck)
  }

  object CargoDeliveryLibrary {
    trait CargoContainer[A] {
      def load(a: A, cargo: String): A
      def unload(a: A): String
    }

    def deliverAll[A: CargoContainer](cargo: List[String], containers: List[A]): Unit =
      ???
  }



  import VehicleSimulationLibrary._,  CargoDeliveryLibrary._

  implicit val pickupTruckCargoContainer: CargoContainer[PickupTruck] =
    new CargoContainer[PickupTruck] {
      override def load(a: PickupTruck, cargo: String): PickupTruck = ???

      override def unload(a: PickupTruck): String = ???
    }

  val cargo = List("item 1", "item 2", "item 3")

  val trucks = createPickupTrucks

  // работаем напрямую с PickupTruck без оберток
  CargoDeliveryLibrary.deliverAll(cargo, trucks)

}
