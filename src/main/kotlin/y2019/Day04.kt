package y2019

import Day

class Day04 : Day<IntRange>(2019, 4) {

    override suspend fun List<String>.parse() = with(first().split("-").map { it.toInt() }) { get(0)..get(1) }

    override suspend fun solve1(input: IntRange) = input.count { it.toString().checkDouble() && it.toString().checkIncreasing() }

    override suspend fun solve2(input: IntRange) = input.count { it.toString().checkIncreasing() && it.toString().checkDoubleStrict() }

    private fun String.checkDouble() = (1 until length).any { get(it - 1) == get(it) }

    private fun String.checkDoubleStrict() = (1 until length).any {
        get(it - 1) == get(it) && (it == 1 || get(it - 2) != get(it)) && (it == length - 1 || get(it + 1) != get(it))
    }

    private fun String.checkIncreasing() = (1 until length).none { get(it - 1).toString().toInt() > get(it).toString().toInt() }
}