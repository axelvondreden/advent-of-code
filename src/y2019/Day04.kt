package y2019

import Day

class Day04 : Day() {

    override val input = (108457..562041)

    override fun solve1() = input.filter { it.toString().checkDouble() && it.toString().checkIncreasing() }.count()

    override fun solve2() = input.filter { it.toString().checkIncreasing() && it.toString().checkDoubleStrict() }.count()

    private fun String.checkDouble() = (1 until length).any { get(it - 1) == get(it) }

    private fun String.checkDoubleStrict() = (1 until length).any {
        get(it - 1) == get(it) && (it == 1 || get(it - 2) != get(it)) && (it == length - 1 || get(it + 1) != get(it))
    }

    private fun String.checkIncreasing() = (1 until length).none { get(it - 1).toString().toInt() > get(it).toString().toInt() }
}