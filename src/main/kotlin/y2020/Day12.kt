package y2020

import Day
import utils.Point
import kotlin.math.abs

class Day12 : Day(2020, 12) {

    override val input = readStrings()

    override fun solve1(): Int {
        val p = move(input)
        return abs(p.x) + abs(p.y)
    }

    override fun solve2(): Int {
        val p = moveByWaypoint(input)
        return abs(p.x) + abs(p.y)
    }

    private fun move(input: List<String>): Point {
        var x = 0
        var y = 0
        var dir = 2
        input.forEach {
            val cmd = it[0]
            val units = it.substring(1).toInt()
            when (cmd) {
                'N' -> y -= units
                'S' -> y += units
                'E' -> x += units
                'W' -> x -= units
                'L' -> dir = (dir - (units / 90) + 4) % 4
                'R' -> dir = (dir + (units / 90)) % 4
                'F' -> {
                    when (dir) {
                        0 -> x -= units
                        1 -> y -= units
                        2 -> x += units
                        3 -> y += units
                    }
                }
            }
        }
        return Point(x, y)
    }

    private fun moveByWaypoint(input: List<String>): Point {
        var wayX = 10
        var wayY = -1
        var x = 0
        var y = 0
        input.forEach {
            val cmd = it[0]
            val units = it.substring(1).toInt()
            when (cmd) {
                'N' -> wayY -= units
                'S' -> wayY += units
                'E' -> wayX += units
                'W' -> wayX -= units
                'L' -> repeat(units / 90) {
                    val tempY = wayY
                    wayY = -wayX
                    wayX = tempY
                }
                'R' -> repeat(units / 90) {
                    val tempY = wayY
                    wayY = wayX
                    wayX = -tempY
                }
                'F' -> repeat(units) {
                    x += wayX
                    y += wayY
                }
            }
        }
        return Point(x, y)
    }
}