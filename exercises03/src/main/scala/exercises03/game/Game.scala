package exercises03.game

object Game {
  def parseState(input: String, number: Int): State = ???

  def action(state: State, number: Int): GameController => Unit = ???

  def completed(state: State): Boolean = ???
}
