package y2021

import Day

class Day06 : Day<List<Int>>(2021, 6) {

    override suspend fun List<String>.parse() = first().split(",").map { it.toInt() }

    override suspend fun solve1(input: List<Int>) = input.calculateSize(80)

    override suspend fun solve2(input: List<Int>) = input.calculateSize(256)

    private fun List<Int>.calculateSize(days: Int): Long {
        var array = LongArray(9)
        for (x in this) array[x]++
        repeat(days) {
            val temp = LongArray(9)
            for (x in 0..8) {
                if (x > 0) {
                    temp[x - 1] += array[x]
                } else {
                    temp[6] += array[x]
                    temp[8] += array[x]
                }
            }
            array = temp
        }
        return array.sum()
    }
}