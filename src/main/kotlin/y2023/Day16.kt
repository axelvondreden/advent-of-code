package y2023

import Day
import utils.Dir
import utils.toCharMatrix
import kotlin.math.max

class Day16 : Day<Array<CharArray>>(2023, 16) {

    override suspend fun List<String>.parse() = toCharMatrix()

    override suspend fun solve1(input: Array<CharArray>) = input.sendLaser(0, 0, Dir.RIGHT)

    override suspend fun solve2(input: Array<CharArray>): Int {
        val hMax = input.indices.maxOf { with(input) { max(sendLaser(it, 0, Dir.DOWN), sendLaser(it, input[0].lastIndex, Dir.UP)) } }
        val vMax = input[0].indices.maxOf { with(input) { max(sendLaser(0, it, Dir.RIGHT), sendLaser(input.lastIndex, it, Dir.LEFT)) } }
        return max(hMax, vMax)
    }

    private fun Array<CharArray>.sendLaser(laserX: Int, laserY: Int, laserDir: Dir): Int {
        val energized = mutableSetOf<Pair<Int, Int>>()
        val laserPositions = mutableSetOf<Laser>()
        val startSymbol = this[laserX][laserY]
        var lasers = when (laserDir) {
            Dir.UP -> when (startSymbol) {
                '/' -> listOf(Laser(laserX, laserY, Dir.RIGHT))
                '\\' -> listOf(Laser(laserX, laserY, Dir.LEFT))
                '-' -> listOf(Laser(laserX, laserY, Dir.LEFT), Laser(laserX, laserY, Dir.RIGHT))
                else -> listOf(Laser(laserX, laserY, Dir.UP))
            }
            Dir.DOWN -> when (startSymbol) {
                '/' -> listOf(Laser(laserX, laserY, Dir.LEFT))
                '\\' -> listOf(Laser(laserX, laserY, Dir.RIGHT))
                '-' -> listOf(Laser(laserX, laserY, Dir.LEFT), Laser(laserX, laserY, Dir.RIGHT))
                else -> listOf(Laser(laserX, laserY, Dir.DOWN))
            }
            Dir.LEFT -> when (startSymbol) {
                '/' -> listOf(Laser(laserX, laserY, Dir.DOWN))
                '\\' -> listOf(Laser(laserX, laserY, Dir.UP))
                '|' -> listOf(Laser(laserX, laserY, Dir.UP), Laser(laserX, laserY, Dir.DOWN))
                else -> listOf(Laser(laserX, laserY, Dir.LEFT))
            }
            Dir.RIGHT -> when (startSymbol) {
                '/' -> listOf(Laser(laserX, laserY, Dir.UP))
                '\\' -> listOf(Laser(laserX, laserY, Dir.DOWN))
                '|' -> listOf(Laser(laserX, laserY, Dir.UP), Laser(laserX, laserY, Dir.DOWN))
                else -> listOf(Laser(laserX, laserY, Dir.RIGHT))
            }
        }
        while (lasers.isNotEmpty()) {
            val newList = mutableListOf<Laser>()
            lasers.forEach { laser ->
                energized += laser.x to laser.y
                laser.move(this).filter { it !in laserPositions }.forEach {
                    newList += it
                    laserPositions += it
                }
            }
            lasers = newList
        }
        return energized.size
    }

    data class Laser(val x: Int, val y: Int, val dir: Dir) {

        fun move(map: Array<CharArray>): List<Laser> {
            val newX = x + when (dir) {
                Dir.RIGHT -> 1
                Dir.LEFT -> -1
                else -> 0
            }
            val newY = y + when (dir) {
                Dir.UP -> -1
                Dir.DOWN -> 1
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