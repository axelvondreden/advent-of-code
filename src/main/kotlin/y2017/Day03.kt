package y2017

import Day
import utils.Point
import utils.IO
import utils.sqrt
import kotlin.math.max

class Day03 : Day() {

    override val input = IO.readString(2017, 3).toInt()

    override fun solve1(): Int {
        var maxWidth = input.sqrt() + 1
        if (maxWidth % 2 == 0) maxWidth++
        val map = generateSpiral(maxWidth)
        val xStart = map.indexOfFirst { it.contains(1) }
        val pStart = Point(xStart, map[xStart].indexOfFirst { it == 1 })
        val xTarget = map.indexOfFirst { it.contains(input) }
        val pTarget = Point(xTarget, map[xTarget].indexOfFirst { it == input })
        return pStart.distance(pTarget)
    }

    override fun solve2(): Int {
        var maxWidth = input.sqrt() + 1
        if (maxWidth % 2 == 0) maxWidth++

        val map = Array(maxWidth) { IntArray(maxWidth) }
        var currentX = maxWidth / 2
        var currentY = maxWidth / 2
        var dir = Dir.RIGHT
        while (currentX in 0 until maxWidth && currentY in 0 until maxWidth) {
            val value = max(map.sumOfNeighbours(currentX, currentY), 1)
            if (value > input) return value
            map[currentX][currentY] = value
            when (dir) {
                Dir.UP -> {
                    if (map.getOrElse(currentX - 1) { IntArray(maxWidth) { 0 } }[currentY] > 0) {
                        currentY--
                    } else {
                        dir = Dir.LEFT
                        currentX--
                    }
                }
                Dir.DOWN -> {
                    if (map.getOrElse(currentX + 1) { IntArray(maxWidth) { 0 } }[currentY] > 0) {
                        currentY++
                    } else {
                        dir = Dir.RIGHT
                        currentX++
                    }
                }
                Dir.LEFT -> {
                    if (map[currentX].getOrElse(currentY + 1) { 0 } > 0) {
                        currentX--
                    } else {
                        dir = Dir.DOWN
                        currentY++
                    }
                }
                Dir.RIGHT -> {
                    if (map[currentX].getOrElse(currentY - 1) { 0 } > 0) {
                        currentX++
                    } else {
                        dir = Dir.UP
                        currentY--
                    }
                }
            }
        }
        return 0
    }

    private fun generateSpiral(width: Int): Array<IntArray> {
        if (width % 2 != 1) throw RuntimeException("oh no!")
        val map = Array(width) { IntArray(width) }
        var currentX = width / 2
        var currentY = width / 2
        var dir = Dir.RIGHT
        var count = 1
        while (currentX in 0 until width && currentY in 0 until width) {
            map[currentX][currentY] = count
            when (dir) {
                Dir.UP -> {
                    if (map.getOrElse(currentX - 1) { IntArray(width) { 0 } }[currentY] > 0) {
                        currentY--
                    } else {
                        dir = Dir.LEFT
                        currentX--
                    }
                }
                Dir.DOWN -> {
                    if (map.getOrElse(currentX + 1) { IntArray(width) { 0 } }[currentY] > 0) {
                        currentY++
                    } else {
                        dir = Dir.RIGHT
                        currentX++
                    }
                }
                Dir.LEFT -> {
                    if (map[currentX].getOrElse(currentY + 1) { 0 } > 0) {
                        currentX--
                    } else {
                        dir = Dir.DOWN
                        currentY++
                    }
                }
                Dir.RIGHT -> {
                    if (map[currentX].getOrElse(currentY - 1) { 0 } > 0) {
                        currentX++
                    } else {
                        dir = Dir.UP
                        currentY--
                    }
                }
            }
            count++
        }
        return map
    }

    private fun Array<IntArray>.sumOfNeighbours(x: Int, y: Int) = (x - 1..x + 1).sumBy { xx ->
        (y - 1..y + 1).sumBy { yy ->
            if (xx != x || yy != y) getOrNull(xx)?.getOrNull(yy) ?: 0 else 0
        }
    }

    enum class Dir {
        UP, DOWN, LEFT, RIGHT
    }
}