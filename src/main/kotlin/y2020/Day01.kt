package y2020

import Day
import Utils

class Day01 : Day() {

    override val input = Utils.readInts(2020, 1)

    override fun solve1(): Int {
        input.forEach { i1 ->
            input.forEach { i2 -> if (i1 + i2 == 2020) return@solve1 i1 * i2 }
        }
        return 0
    }

    override fun solve2() = 0
}