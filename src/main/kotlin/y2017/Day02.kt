package y2017

import Day
import Utils

class Day02 : Day() {

    override val input = Utils.readStrings(2017, 2)

    override fun solve1() = input.sumBy { rowChecksum(it) }

    override fun solve2() = input.sumBy { rowChecksum2(it) }

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