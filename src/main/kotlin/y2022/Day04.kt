package y2022

import Day

class Day04 : Day<List<List<IntRange>>>(2022, 4) {

    override suspend fun List<String>.parse() = map { line ->
        line.split(",").map { area -> area.split("-").let { it[0].toInt()..it[1].toInt() } }
    }

    override suspend fun solve1(input: List<List<IntRange>>) = input.count {
        (it[0].contains(it[1].first) && it[0].contains(it[1].last)) || (it[1].contains(it[0].first) && it[1].contains(it[0].last))
    }

    override suspend fun solve2(input: List<List<IntRange>>) = input.count { it[0].intersect(it[1]).isNotEmpty() }
}