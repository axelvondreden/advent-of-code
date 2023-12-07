package y2020

import Day

class Day05 : Day<List<String>>(2020, 5) {

    override val input = readStrings()

    override fun solve1(input: List<String>) = input.maxOf { it.getSeatId() }

    override fun solve2(input: List<String>): Int {
        val ids = input.map { it.getSeatId() }.sorted()
        var last = ids.first()
        (1 until ids.size).forEach { i ->
            if (ids[i] == last + 1) last = ids[i]
            else return last + 1
        }
        return 0
    }

    private fun String.getSeatId(): Int {
        val row = substring(0, 7)
        val col = substring(7)
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