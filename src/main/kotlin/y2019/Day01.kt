package y2019

import Day

class Day01 : Day<List<String>>(2019, 1) {

    override fun List<String>.parse() = this

    override fun solve1(input: List<String>) = input.sumOf { (it.toInt() / 3) - 2 }

    override fun solve2(input: List<String>) = input.sumOf { it.toInt().calcFuel() }

    private fun Int.calcFuel(): Int {
        val rem = (this / 3) - 2
        return if (rem > 0) rem + rem.calcFuel() else 0
    }
}