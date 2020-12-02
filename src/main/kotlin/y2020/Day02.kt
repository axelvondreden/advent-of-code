package y2020

import Day
import Utils

class Day02 : Day() {

    override val input = Utils.readStrings(2020, 2).map {
        val split = it.split(":")
        val range = split[0].split(" ")[0].split("-")
        Password(range[0].toInt()..range[1].toInt(), split[0].split(" ")[1][0], split[1].trim())
    }

    override fun solve1() = input.count { it.isValid() }

    override fun solve2() = input.count { it.isValid2() }

    data class Password(val range: IntRange, val char: Char, val password: String)

    private fun Password.isValid() = password.count { it == char } in range
    private fun Password.isValid2() = listOf(password[range.first - 1], password[range.last - 1]).count { it == char } == 1
}