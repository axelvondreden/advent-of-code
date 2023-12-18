package y2023

import Day
import utils.Point
import kotlin.math.abs

class Day18 : Day<List<String>>(2023, 18) {

    override fun List<String>.parse() = this

    override fun solve1(input: List<String>) = input.calculateArea { line ->
        line.split(' ').let { parts ->
            directionsMap.getValue(parts[0].first()) to parts[1].toInt()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun solve2(input: List<String>) = input.calculateArea { line ->
        colorRegex.find(line)
            ?.let { it.groupValues[2].toInt() to it.groupValues[1].hexToInt() }
            ?: error("Big fail")
    }

    private fun List<String>.calculateArea(parser: (String) -> Pair<Int, Int>): Long {
        var x = 0
        var y = 0
        var len = 0L
        var point = Point(x, y)
        val points = mutableListOf<Point>()

        forEach { line ->
            val (dir, count) = parser(line)
            val (dx, dy) = directions[dir]

            x += dx * count
            y += dy * count
            len += point.distance(Point(x, y))

            point = Point(x, y)
            points.add(point)
        }

        // Pick's theorem - see Day10 for more information.
        val innerArea = gaussArea(points) + 1 - len / 2
        return len + innerArea
    }

    private fun gaussArea(pp: List<Point>): Long {
        val last = pp.lastIndex
        val area = (0..<last).fold(0L) { acc, i ->
            acc + pp[i].x * pp[i + 1].y - pp[i + 1].x * pp[i].y
        } + pp[last].x * pp[0].y - pp[0].x * pp[last].y

        return abs(area) / 2
    }

    companion object {
        private val colorRegex = Regex("#([0-9a-f]{5})(\\d)")
        private val directions = arrayOf((1 to 0), (0 to 1), (-1 to 0), (0 to -1))
        private val directionsMap = hashMapOf('R' to 0, 'D' to 1, 'L' to 2, 'U' to 3)
    }
}