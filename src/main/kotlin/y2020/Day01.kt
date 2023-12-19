package y2020

import Day

class Day01 : Day<List<Int>>(2020, 1) {

    override suspend fun List<String>.parse() = map { it.toInt() }

    override suspend fun solve1(input: List<Int>): Int {
        input.forEach { i1 ->
            input.forEach { i2 -> if (i1 + i2 == 2020) return@solve1 i1 * i2 }
        }
        return 0
    }

    override suspend fun solve2(input: List<Int>): Int {
        input.forEach { i1 ->
            input.forEach { i2 ->
                input.forEach { i3 -> if (i1 + i2 + i3 == 2020) return@solve2 i1 * i2 * i3}
            }
        }
        return 0
    }
}