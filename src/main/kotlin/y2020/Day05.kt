package y2020

import Day

class Day05 : Day(2020, 5) {

    override val input = readStrings()

    override fun solve1() = input.map { getSeatId(it) }.maxOrNull()!!

    override fun solve2(): Int {
        val ids = input.map { getSeatId(it) }.sorted()
        var last = ids.first()
        for (i in 1 until ids.size) {
            if (ids[i] == last + 1) last = ids[i]
            else return last + 1
        }
        return 0
    }

    private fun getSeatId(specifier: String): Int {
        val row = specifier.substring(0, 7)
        val col = specifier.substring(7)
        var rowLower = 0
        var rowUpper = 127
        row.forEach {
            when (it) {
                'F' -> rowUpper -= ((rowUpper - rowLower) / 2) + 1
                'B' -> rowLower += ((rowUpper - rowLower) / 2) + 1
            }
        }
        var colLower = 0
        var colUpper = 7
        col.forEach {
            when (it) {
                'L' -> colUpper -= ((colUpper - colLower) / 2) + 1
                'R' -> colLower += ((colUpper - colLower) / 2) + 1
            }
        }
        return rowLower * 8 + colLower
    }
}