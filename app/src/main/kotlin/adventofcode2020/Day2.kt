package adventofcode2020

import java.util.regex.Pattern

data class Line(val min: Int, val max: Int, val letter: Char, val password: String) {
  fun valid(): Boolean {
    return password.count { it == letter } in min..max
  }
}

data class LinePart2(val min: Int, val max: Int, val letter: Char, val password: String) {
  fun valid(): Boolean {
    return (password[min - 1] == letter || password[max - 1] == letter) && password[min - 1] != password[max - 1]
  }
}

private val p = Pattern.compile("([0-9]+)-([0-9]+) ([a-z]): (\\w+)")

fun isValid(line: String): Boolean {
  val m = p.matcher(line)
  if (m.matches()) {
    val min = m.group(1).toInt()
    val max = m.group(2).toInt()
    val letter = m.group(3)[0]
    val password = m.group(4)
    val linee = LinePart2(min, max, letter, password)
    return linee.valid()
  } else {
    throw IllegalArgumentException("Unparseable line $line")
  }
}

class Foo

fun main() {
  val answer = Foo::class.java.getResource("/day2_password_philosophy")
    .openStream()
    .bufferedReader()
    .lines()
    .filter(String::isNotBlank)
    .filter{ line -> isValid(line) }
    .count()

  println(answer)
}