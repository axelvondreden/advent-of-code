package y2020

import Day

class Day02 : Day<List<Day02.Password>>(2020, 2) {

    override suspend fun List<String>.parse() = map {
        val split = it.split(":")
        val range = split[0].split(" ")[0].split("-")
        Password(range[0].toInt()..range[1].toInt(), split[0].split(" ")[1][0], split[1].trim())
    }

    override suspend fun solve1(input: List<Password>) = input.count { it.isValid() }

    override suspend fun solve2(input: List<Password>) = input.count { it.isValid2() }

    data class Password(val range: IntRange, val char: Char, val password: String) {
        fun isValid() = password.count { it == char } in range
        fun isValid2() = listOf(password[range.first - 1], password[range.last - 1]).count { it == char } == 1
    }
}