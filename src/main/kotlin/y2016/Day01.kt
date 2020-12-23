package y2016

import Day
import utils.Point
import utils.IO
import kotlin.math.abs

class Day01 : Day() {

    override val input = IO.readString(2016, 1).split(", ")

    override fun solve1(): Int {
        var x = 0
        var y = 0
        var dir = 0
        for (inst in input) {
            val d = inst[0]
            val steps = inst.substring(1).toInt()
            when (d) {
                'L' -> dir--
                'R' -> dir++
            }
            if (dir < 0) {
                dir = 3
            }
            if (dir > 3) {
                dir = 0
            }
            when (dir) {
                0 -> y -= steps
                1 -> x += steps
                2 -> y += steps
                3 -> x -= steps
            }
        }
        return abs(x) + abs(y)
    }

    override fun solve2(): Int {
        var x = 0
        var y = 0
        var dir = 0
        val visited = mutableSetOf<Point>()
        for (inst in input) {
            val d = inst[0]
            val steps = inst.substring(1).toInt()
            when (d) {
                'L' -> dir--
                'R' -> dir++
            }
            if (dir < 0) {
                dir = 3
            }
            if (dir > 3) {
                dir = 0
            }
            repeat(steps) {
                when (dir) {
                    0 -> y--
                    1 -> x++
                    2 -> y++
                    3 -> x--
                }
                if (!visited.add(Point(x, y))) {
                    return abs(x) + abs(y)
                }
            }
        }
        return 0
    }
}