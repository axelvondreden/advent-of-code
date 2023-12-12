package y2023

import Day
import utils.*
import kotlin.math.max
import kotlin.math.min

class Day11 : Day<Array<CharArray>>(2023, 11) {

    override fun List<String>.parse() = toCharMatrix()

    override fun solve1(input: Array<CharArray>): Long {
        val emptyColumns = input.indices.filter { x -> input[x].all { it == '.' } }
        val emptyRows = input[0].indices.filter { y -> input.indices.all { x -> input[x][y] == '.' } }
        val pairs = input.findPoints('#').getAllPairs()
        return pairs.sumOf { pair ->
            val xRange = min(pair.first.x, pair.second.x)..max(pair.first.x, pair.second.x)
            val yRange = min(pair.first.y, pair.second.y)..max(pair.first.y, pair.second.y)
            val colsBetween = emptyColumns.count { it in xRange }
            val rowsBetween = emptyRows.count { it in yRange }
            val p1 = Point(xRange.first, yRange.first)
            val p2 = Point(xRange.last + (colsBetween), yRange.last + (rowsBetween))
            p1.distance(p2)
        }
    }

    override fun solve2(input: Array<CharArray>): Long {
        val emptyColumns = input.indices.filter { x -> input[x].all { it == '.' } }
        val emptyRows = input[0].indices.filter { y -> input.indices.all { x -> input[x][y] == '.' } }
        val pairs = input.findPoints('#').getAllPairs()
        return pairs.sumOf { pair ->
            val xRange = min(pair.first.x, pair.second.x)..max(pair.first.x, pair.second.x)
            val yRange = min(pair.first.y, pair.second.y)..max(pair.first.y, pair.second.y)
            val colsBetween = emptyColumns.count { it in xRange }
            val rowsBetween = emptyRows.count { it in yRange }
            val p1 = Point(xRange.first, yRange.first)
            val p2 = Point(xRange.last + (colsBetween * 999999), yRange.last + (rowsBetween * 999999))
            p1.distance(p2)
        }
    }
}