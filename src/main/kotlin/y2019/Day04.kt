package y2019

import Day

class Day04 : Day<List<String>>(2019, 4) {

    override val input = with(readString().split("-").map { it.toInt() }) { get(0)..get(1) }

    override fun solve1(input: List<String>) = input.count { it.toString().checkDouble() && it.toString().checkIncreasing() }

    override fun solve2(input: List<String>) = input.count { it.toString().checkIncreasing() && it.toString().checkDoubleStrict() }

    private fun String.checkDouble() = (1 until length).any { get(it - 1) == get(it) }

    private fun String.checkDoubleStrict() = (1 until length).any {
        get(it - 1) == get(it) && (it == 1 || get(it - 2) != get(it)) && (it == length - 1 || get(it + 1) != get(it))
    }

    private fun String.checkIncreasing() = (1 until length).none { get(it - 1).toString().toInt() > get(it).toString().toInt() }
}