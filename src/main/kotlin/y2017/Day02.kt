package y2017

import Day

class Day02 : Day<Any?>(2017, 2) {

    override val input = readStrings()

    override fun solve1(input: List<String>) = input.sumOf { rowChecksum(it) }

    override fun solve2(input: List<String>) = input.sumOf { rowChecksum2(it) }

    private fun rowChecksum(row: String): Int {
        val values = row.split(Regex("\\s+")).map { it.toInt() }
        return values.maxOrNull()!! - values.minOrNull()!!
    }

    private fun rowChecksum2(row: String): Int {
        val values = row.split(Regex("\\s+")).map { it.toInt() }
        values.forEach { v1 ->
            values.forEach { v2 ->
                if (v1 != v2 && v1 % v2 == 0) return v1 / v2
            }
        }
        return 0
    }
}