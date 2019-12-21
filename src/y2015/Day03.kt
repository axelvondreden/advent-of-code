package y2015

import Day
import Point

class Day03: Day() {

    override val input = Utils.readString(2015, 3).toCharArray().asList()

    override fun solve1(): Int {
        val map = mutableMapOf(Pair(Point(0, 0), 1))
        runSteps(input, map)
        return map.size
    }

    override fun solve2(): Int {
        val steps1 = input.filterIndexed { index, _ -> index % 2 == 0 }
        val steps2 = input.filterIndexed { index, _ -> index % 2 == 1 }
        val map = mutableMapOf(Pair(Point(0, 0), 1))
        runSteps(steps1, map)
        runSteps(steps2, map)
        return map.size
    }

    private fun runSteps(steps: List<Char>, map: MutableMap<Point, Int>) {
        var point = Point(0, 0)
        for (c in steps) {
            when (c) {
                '<' -> point = Point(point.x - 1, point.y)
                '>' -> point = Point(point.x + 1, point.y)
                '^' -> point = Point(point.x, point.y - 1)
                'v' -> point = Point(point.x, point.y + 1)
            }
            map[point] = map.getOrDefault(point, 0) + 1
        }
    }
}