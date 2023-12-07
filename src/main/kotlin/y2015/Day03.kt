package y2015

import Day
import utils.Point

class Day03: Day<List<Char>>(2015, 3) {

    override fun List<String>.parse() = this[0].toCharArray().asList()

    override fun solve1(input: List<Char>): Int {
        with(mutableMapOf(Point(0, 0) to 1)) {
            runSteps(input)
            return size
        }
    }

    override fun solve2(input: List<Char>): Int {
        with(mutableMapOf(Point(0, 0) to 1)) {
            runSteps(input.filterIndexed { index, _ -> index % 2 == 0 })
            runSteps(input.filterIndexed { index, _ -> index % 2 == 1 })
            return size
        }
    }

    private fun MutableMap<Point, Int>.runSteps(steps: List<Char>) {
        var point = Point(0, 0)
        steps.forEach {
            when (it) {
                '<' -> point += Point(-1, 0)
                '>' -> point += Point(1, 0)
                '^' -> point += Point(0, -1)
                'v' -> point += Point(0, 1)
            }
            put(point, getOrDefault(point, 0) + 1)
        }
    }
}