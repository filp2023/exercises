package exercises03.game

import scala.annotation.tailrec
import scala.util.Random

object Starter extends App {
  def unsafeRun(number: Int, controller: GameController): Unit = {
    @tailrec
    def recursive(completed: Boolean): Unit =
      if (completed) ()
      else {
        controller.askNumber()
        val state = Game.parseState(controller.nextLine(), number)
        Game.action(state, number)(controller)
        recursive(completed = Game.completed(state))
      }

    recursive(completed = false)
  }

  val number: Int = Random.nextInt(10) + 1
  unsafeRun(number, GameController.live)
}
