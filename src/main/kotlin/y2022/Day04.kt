package y2022

import Day

class Day04 : Day(2022, 4) {

    override val input = readStrings().map { line ->
        line.split(",").map { area -> area.split("-").let { it[0].toInt()..it[1].toInt() } }
    }

    override fun solve1() = input.count {
        (it[0].contains(it[1].first) && it[0].contains(it[1].last)) || (it[1].contains(it[0].first) && it[1].contains(it[0].last))
    }

    override fun solve2() = input.count { it[0].intersect(it[1]).isNotEmpty() }
}