package y2016

import Day
import utils.Dir
import utils.Point
import utils.Turn
import kotlin.math.abs

class Day01 : Day<List<Day01.Step>>(2016, 1) {

    override suspend fun List<String>.parse() = first().split(", ").map {
        Step(if (it[0] == 'L') Turn.LEFT else Turn.RIGHT, it.drop(1).toInt())
    }

    override suspend fun solve1(input: List<Step>): Long {
        var point = Point(0, 0)
        var dir = Dir.UP
        input.forEach { step ->
            dir = dir.turn(step.turn)
            point = when (dir) {
                Dir.LEFT -> point left step.amount
                Dir.RIGHT -> point right step.amount
                Dir.UP -> point up step.amount
                Dir.DOWN -> point down step.amount
            }
        }
        return abs(point.x) + abs(point.y)
    }

    override suspend fun solve2(input: List<Step>): Long {
        var point = Point(0, 0)
        var dir = Dir.UP
        val visited = mutableSetOf<Point>()
        input.forEach { step ->
            dir = dir.turn(step.turn)
            repeat(step.amount) {
                point = when (dir) {
                    Dir.LEFT -> point left 1
                    Dir.RIGHT -> point right 1
                    Dir.UP -> point up 1
                    Dir.DOWN -> point down 1
                }
                if (!visited.add(Point(point.x, point.y))) return abs(point.x) + abs(point.y)
            }
        }
        return 0
    }

    data class Step(val turn: Turn, val amount: Int)
}