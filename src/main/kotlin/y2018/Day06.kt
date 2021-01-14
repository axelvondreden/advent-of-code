package y2018

import Day
import utils.Point

class Day06 : Day(2018, 6) {

    override val input = readStrings().map { Point(it, ", ") }

    private val xValues = input.map { it.x }
    private val yValues = input.map { it.y }
    private val xRange = xValues.minOrNull()!!..xValues.maxOrNull()!!
    private val yRange = yValues.minOrNull()!!..yValues.maxOrNull()!!

    override fun solve1(): Int {
        val infinite = mutableSetOf<Point>()
        return xRange.flatMap { x ->
            yRange.map { y ->
                val closest = input.map { it to it.distance(Point(x, y)) }.sortedBy { it.second }.take(2)
                if (isEdge(Point(x, y))) {
                    infinite.add(closest[0].first)
                }
                closest[0].first.takeUnless { closest[0].second == closest[1].second }
            }
        }.filterNot { it in infinite }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.value
    }

    override fun solve2(): Int = xRange.flatMap { x ->
        yRange.map { y ->
            input.map { it.distance(Point(x, y)) }.sum()
        }
    }.filter { it < 10000 }.count()

    private fun isEdge(p: Point) = p.x == xRange.first || p.x == xRange.last || p.y == yRange.first || p.y == yRange.last

}