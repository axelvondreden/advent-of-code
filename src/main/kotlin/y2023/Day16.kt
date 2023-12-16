package y2023

import Day
import utils.Dir
import utils.toCharMatrix

class Day16 : Day<Array<CharArray>>(2023, 16) {

    override fun List<String>.parse() = toCharMatrix()

    override fun solve1(input: Array<CharArray>): Int {
        val energized = mutableSetOf<Pair<Int, Int>>()
        var lasers = listOf(Laser(0, 0, Dir.RIGHT))
        while (lasers.isNotEmpty()) {
            val newList = mutableListOf<Laser>()
            lasers.forEach {
                energized += it.x to it.y
                newList += it.move(input)
            }
            lasers = newList
        }
        return energized.size
    }

    override fun solve2(input: Array<CharArray>): Int = 0

    class Laser(val x: Int, val y: Int, val dir: Dir) {

        fun move(map: Array<CharArray>): List<Laser> {
            val newX = x + when (dir) {
                Dir.UP -> -1
                Dir.DOWN -> 1
                else -> 0
            }
            val newY = y + when (dir) {
                Dir.LEFT -> -1
                Dir.RIGHT -> 1
                else -> 0
            }
            if (newX in map.indices && newY in map[0].indices) {
                val c = map[newX][newY]
                when (c) {
                    '.' -> return listOf(Laser(newX, newY, dir))
                    '/' -> return listOf(
                        Laser(
                            newX, newY, when (dir) {
                                Dir.LEFT -> Dir.DOWN
                                Dir.RIGHT -> Dir.UP
                                Dir.UP -> Dir.RIGHT
                                Dir.DOWN -> Dir.LEFT
                            }
                        )
                    )
                    '\\' -> return listOf(
                        Laser(
                            newX, newY, when (dir) {
                                Dir.LEFT -> Dir.UP
                                Dir.RIGHT -> Dir.DOWN
                                Dir.UP -> Dir.LEFT
                                Dir.DOWN -> Dir.RIGHT
                            }
                        )
                    )
                    '-' -> return when (dir) {
                        Dir.LEFT, Dir.RIGHT -> listOf(Laser(newX, newY, dir))
                        Dir.UP, Dir.DOWN -> listOf(Laser(newX, newY, Dir.LEFT), Laser(newX, newY, Dir.RIGHT))
                    }
                    '|' -> return when (dir) {
                        Dir.UP, Dir.DOWN -> listOf(Laser(newX, newY, dir))
                        Dir.LEFT, Dir.RIGHT -> listOf(Laser(newX, newY, Dir.UP), Laser(newX, newY, Dir.DOWN))
                    }
                    else -> error("Big Fail")
                }
            }
            return emptyList()
        }
    }
}