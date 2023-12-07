package y2020

import Day

class Day01 : Day<List<String>>(2020, 1) {

    override val input = readInts()

    override fun solve1(input: List<String>): Int {
        input.forEach { i1 ->
            input.forEach { i2 -> if (i1 + i2 == 2020) return@solve1 i1 * i2 }
        }
        return 0
    }

    override fun solve2(input: List<String>): Int {
        input.forEach { i1 ->
            input.forEach { i2 ->
                input.forEach { i3 -> if (i1 + i2 + i3 == 2020) return@solve2 i1 * i2 * i3}
            }
        }
        return 0
    }
}