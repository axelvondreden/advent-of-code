package y2016

import Day
import utils.Point
import kotlin.math.abs

class Day01 : Day<Any?>(2016, 1) {

    override val input = readString().split(", ")

    override fun solve1(input: List<String>): Int {
        var x = 0
        var y = 0
        var dir = 0
        input.forEach { inst ->
            val d = inst[0]
            val steps = inst.substring(1).toInt()
            when (d) {
                'L' -> dir--
                'R' -> dir++
            }
            if (dir < 0) dir = 3
            if (dir > 3) dir = 0
            when (dir % 4) {
                0 -> y -= steps
                1 -> x += steps
                2 -> y += steps
                3 -> x -= steps
            }
        }
        return abs(x) + abs(y)
    }

    override fun solve2(input: List<String>): Long {
        var x = 0L
        var y = 0L
        var dir = 0
        val visited = mutableSetOf<Point>()
        input.forEach { inst ->
            val d = inst[0]
            val steps = inst.substring(1).toInt()
            when (d) {
                'L' -> dir--
                'R' -> dir++
            }
            if (dir < 0) dir = 3
            if (dir > 3) dir = 0
            repeat(steps) {
                when (dir) {
                    0 -> y--
                    1 -> x++
                    2 -> y++
                    3 -> x--
                }
                if (!visited.add(Point(x, y))) return abs(x) + abs(y)
            }
        }
        return 0
    }
}