// Условный переход

val age = 13

if (age < 18) println("Underaged")
//?? где Unit

if (age < 10) println("Underaged") else println("Adult")

val label =
  if (age < 18)
    "Underaged"
  else
    "Adult"
