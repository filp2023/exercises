package exercises03.game

sealed trait State
case object GiveUp          extends State
case object WrongInput      extends State
case object NumberIsBigger  extends State
case object NumberIsSmaller extends State
case object Guessed         extends State
