package y2019

import Day

class Day01 : Day(2019, 1) {

    override val input = readStrings()

    override fun solve1() = input.map { (it.toInt() / 3) - 2 }.sum()

    override fun solve2() = input.map { calcFuel(it.toInt()) }.sum()

    private fun calcFuel(fuel: Int): Int {
        val rest = (fuel / 3) - 2
        return if (rest > 0) rest + calcFuel(rest) else 0
    }
}