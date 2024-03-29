package y2018

import Day
import utils.Point
import utils.Point3D

class Day11 : Day<Int>(2018, 11) {

    override suspend fun List<String>.parse() = first().toInt()
    private val cache = mutableMapOf<String, Int>()

    override suspend fun solve1(input: Int) = highest3x3Chunk(input)

    override suspend fun solve2(input: Int) = highestNxNChunk(input)

    private fun highest3x3Chunk(input: Int): String {
        var best = Point(0, 0)
        var bestScore = Int.MIN_VALUE
        (1..298).forEach { x ->
            (1..298).forEach { y ->
                val score = calcNxNScore(input, x, y, 2)
                if (score > bestScore) {
                    bestScore = score
                    best = Point(x, y)
                }
            }
        }
        return "${best.x},${best.y}"
    }

    private fun highestNxNChunk(input: Int): String {
        var best = Point3D(0, 0, 0)
        var bestScore = Int.MIN_VALUE
        (1..13).forEach { size ->
            (1..300 - size).forEach { x ->
                (1..300 - size).forEach { y ->
                    val score = calcNxNScore(input, x, y, size)
                    if (score > bestScore) {
                        bestScore = score
                        best = Point3D(x, y, size + 1)
                    }
                }
            }
        }
        return "${best.x},${best.y},${best.z}"
    }

    private fun calcNxNScore(input: Int, x: Int, y: Int, size: Int) =
        (x..x + size).sumOf { xx -> (y..y + size).sumOf { yy -> input.getPowerLvl(xx, yy) } }

    private fun Int.getPowerLvl(x: Int, y: Int) = cache.getOrPut("$x,$y") {
        val rackId = x + 10
        var pwrLvl = ((rackId * y) + this) * rackId
        pwrLvl = pwrLvl.toString().reversed().getOrNull(2)?.toString()?.toInt() ?: 0
        pwrLvl - 5
    }
}