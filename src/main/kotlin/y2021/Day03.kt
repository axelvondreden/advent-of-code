package y2021

import Day

class Day03 : Day<Any?>(2021, 3) {

    override val input = readStrings()
    val length = input[0].length

    override fun solve1(input: List<String>): Int {
        val gamma = (0 until length).map { i -> if (input.count { it[i] == '1' } > input.size / 2) '1' else '0' }
            .joinToString("")
        val epsilon = (0 until length).map { i -> if (input.count { it[i] == '1' } > input.size / 2) '0' else '1' }
            .joinToString("")
        return gamma.toInt(2) * epsilon.toInt(2)
    }

    override fun solve2(input: List<String>): Int {
        val oxygenGeneratorRating = calcRating(input, true, '1').toInt(2)
        val co2ScrubberRating = calcRating(input, false, '0').toInt(2)
        return oxygenGeneratorRating * co2ScrubberRating
    }

    private fun calcRating(list: List<String>, takeBigger: Boolean, deciderChar: Char): String {
        var readings = list
        var index = 0
        while (readings.size > 1) {
            val zeroes = readings.count { it[index] == '0' }
            val ones = readings.count { it[index] == '1' }
            val filter = when {
                zeroes == ones -> deciderChar
                takeBigger -> if (zeroes > ones) '0' else '1'
                zeroes < ones -> '0'
                else -> '1'
            }
            readings = readings.filter { it[index] == filter }
            index++
            if (index == readings[0].length) index = 0
        }
        return readings[0]
    }
}