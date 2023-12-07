package y2018

import Day
import utils.Point

class Day06 : Day<List<Point>>(2018, 6) {

    override fun List<String>.parse() = map { Point(it, ", ") }

    override fun solve1(input: List<Point>): Int {
        val xValues = input.map { it.x }
        val yValues = input.map { it.y }
        val xRange = xValues.minOrNull()!!..xValues.maxOrNull()!!
        val yRange = yValues.minOrNull()!!..yValues.maxOrNull()!!
        val infinite = mutableSetOf<Point>()
        return xRange.flatMap { x ->
            yRange.map { y ->
                val closest = input.map { it to it.distance(Point(x, y)) }.sortedBy { it.second }.take(2)
                if (Point(x, y).isEdge(xRange, yRange)) {
                    infinite.add(closest[0].first)
                }
                closest[0].first.takeUnless { closest[0].second == closest[1].second }
            }
        }.filterNot { it in infinite }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.value
    }

    override fun solve2(input: List<Point>): Int {
        val xValues = input.map { it.x }
        val yValues = input.map { it.y }
        val xRange = xValues.minOrNull()!!..xValues.maxOrNull()!!
        val yRange = yValues.minOrNull()!!..yValues.maxOrNull()!!
        return xRange.flatMap { x ->
            yRange.map { y ->
                input.sumOf { it.distance(Point(x, y)) }
            }
        }.count { it < 10000 }
    }

    private fun Point.isEdge(xRange: LongRange, yRange: LongRange) = x == xRange.first || x == xRange.last || y == yRange.first || y == yRange.last
}