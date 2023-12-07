package y2022

import Day
import utils.Point
import kotlin.math.abs

class Day09 : Day<Any?>(2022, 9) {

    override val input = readStrings().map { line -> line.split(" ").let { it[0] to it[1].toInt() } }

    override fun solve1(input: List<String>): Int {
        var head = Point(0, 0)
        var tail = Point(0, 0)
        val tailSpots = mutableSetOf(tail.copy())
        input.forEach { entry ->
            repeat(entry.second) {
                head = head.move(entry.first)
                tail = calcTail(head, tail)
                tailSpots += tail.copy()
            }
        }
        return tailSpots.size
    }

    override fun solve2(input: List<String>): Int {
        val knots = Array(10) { Point(0, 0) }
        val tailSpots = mutableSetOf(knots.last().copy())
        input.forEach { entry ->
            repeat(entry.second) {
                knots[0] = knots[0].move(entry.first)
                for (i in 1..knots.lastIndex) {
                    knots[i] = calcTail(knots[i - 1], knots[i])
                }
                tailSpots += knots.last().copy()
            }
        }
        return tailSpots.size
    }

    private fun calcTail(head: Point, tail: Point): Point {
        val dx = abs(head.x - tail.x)
        val dy = abs(head.y - tail.y)
        return when {
            dx > 0 && dy > 0 && (dx > 1 || dy > 1) -> {
                Point(tail.x + (head.x - tail.x).coerceIn(-1..1L), tail.y + (head.y - tail.y).coerceIn(-1..1L))
            }
            dx > 1 -> Point(tail.x + (head.x - tail.x).coerceIn(-1..1L), tail.y)
            dy > 1 -> Point(tail.x, tail.y + (head.y - tail.y).coerceIn(-1..1L))
            else -> tail.copy()
        }
    }

    private fun Point.move(dir: String) = when (dir) {
        "L" -> copy(x = x - 1)
        "R" -> copy(x = x + 1)
        "U" -> copy(y = y - 1)
        "D" -> copy(y = y + 1)
        else -> error("oh no")
    }
}