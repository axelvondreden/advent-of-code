package y2017

import Day
import utils.Point
import utils.toCharMatrix

class Day22 : Day<Array<CharArray>>(2017, 22) {

    override suspend fun List<String>.parse() = toCharMatrix()

    private val weakNodes = mutableListOf<Point>()
    private val flaggedNodes = mutableListOf<Point>()

    private var infectionCounter = 0

    override suspend fun solve1(input: Array<CharArray>): Int {
        infectionCounter = 0
        var current = Point((input.size / 2), (input[0].size / 2))
        val infectedNodes = input.flatMapIndexed { x, chars ->
            chars.mapIndexed { y, c -> if (c == '#') Point(x, y) else null }.filterNotNull()
        }
        val list = infectedNodes.toMutableList()
        var dir = Dir.UP
        repeat(10000) {
            val ret = list.burst(current, dir)
            current = ret.first
            dir = ret.second
        }
        return infectionCounter
    }

    override suspend fun solve2(input: Array<CharArray>): Int {
        infectionCounter = 0
        var current = Point((input.size / 2), (input[0].size / 2))
        val infectedNodes = input.flatMapIndexed { x, chars ->
            chars.mapIndexed { y, c -> if (c == '#') Point(x, y) else null }.filterNotNull()
        }
        val infected = infectedNodes.toMutableList()
        var dir = Dir.UP
        repeat(10000000) {
            val ret = infected.burst2(current, dir)
            current = ret.first
            dir = ret.second
        }
        return infectionCounter
    }

    private fun MutableList<Point>.burst(current: Point, dir: Dir): Pair<Point, Dir> {
        val newDir: Dir
        if (current in this) {
            newDir = dir.right()
            remove(current)
        } else {
            newDir = dir.left()
            add(current)
            infectionCounter++
        }
        return current + newDir.move() to newDir
    }

    private fun MutableList<Point>.burst2(current: Point, dir: Dir): Pair<Point, Dir> {
        val newDir: Dir
        when (current) {
            in this -> {
                newDir = dir.right()
                remove(current)
                flaggedNodes.add(current)
            }
            in flaggedNodes -> {
                newDir = dir.right().right()
                flaggedNodes.remove(current)
            }
            in weakNodes -> {
                newDir = dir
                weakNodes.remove(current)
                add(current)
                infectionCounter++
            }
            else -> {
                newDir = dir.left()
                weakNodes.add(current)
            }
        }
        return current + newDir.move() to newDir
    }

    private enum class Dir {
        LEFT, RIGHT, UP, DOWN;

        fun left() = when (this) {
            LEFT -> DOWN
            RIGHT -> UP
            UP -> LEFT
            DOWN -> RIGHT
        }

        fun right() = when (this) {
            LEFT -> UP
            RIGHT -> DOWN
            UP -> RIGHT
            DOWN -> LEFT
        }

        fun move() = when (this) {
            LEFT -> Point(-1, 0)
            RIGHT -> Point(1, 0)
            UP -> Point(0, -1)
            DOWN -> Point(0, 1)
        }
    }
}
