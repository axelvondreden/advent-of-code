package y2024

import Day

class Day03 : Day<String>(2024, 3) {

    override suspend fun List<String>.parse() = joinToString("")

    override suspend fun solve1(input: String) =
        Regex("mul\\(\\d+,\\d+\\)").findAll(input)
            .map { it.value }
            .sumOf { it.substring(4, it.indexOf(',')).toInt() * it.substring(it.indexOf(',') + 1, it.length - 1).toInt() }

    override suspend fun solve2(input: String): Int {
        val operations = Regex("mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\)").findAll(input).map { it.value }.toList()
        var active = true
        var sum = 0
        operations.forEach {
            when {
                it.startsWith("do()") -> active = true
                it.startsWith("don't()") -> active = false
                else -> {
                    if (active) {
                        sum += it.substring(4, it.indexOf(',')).toInt() * it.substring(it.indexOf(',') + 1, it.length - 1).toInt()
                    }
                }
            }
        }
        return sum
    }
}