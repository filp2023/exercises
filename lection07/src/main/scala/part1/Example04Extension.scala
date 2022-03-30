package part1

object Example04Extension {
  // Проблемы с расширением

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
    trait CargoContainer {
      def load(cargo: String): Unit
      def unload(): String
    }

    def deliverAll(cargo: List[String], containers: List[CargoContainer]): Unit =
      ???
  }


  import VehicleSimulationLibrary._, CargoDeliveryLibrary._

  val cargo = List("item 1", "item 2", "item 3")

  val trucks = createPickupTrucks

//  won't compile
//  CargoDeliveryLibrary.deliverAll(cargo, trucks)

// Нужно писать адаптер и перепаковывать объекты

}
