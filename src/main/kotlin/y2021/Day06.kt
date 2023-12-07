package y2021

import Day

class Day06 : Day<Any?>(2021, 6) {

    override val input = readString().split(",").map { it.toInt() }

    override fun solve1(input: List<String>) = calculateSize(80)

    override fun solve2(input: List<String>) = calculateSize(256)

    private fun calculateSize(days: Int): Long {
        var array = LongArray(9)
        for (x in input) array[x]++
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