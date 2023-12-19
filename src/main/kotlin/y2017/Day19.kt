package y2017

import Day
import utils.Point
import utils.toCharMatrix

class Day19 : Day<Array<CharArray>>(2017, 19) {

    override suspend fun List<String>.parse() = toCharMatrix()

    override suspend fun solve1(input: Array<CharArray>): String {
        val startX = input.mapIndexed { index, chars -> index to chars[0] }.first { it.second == '|' }.first
        var point = Point(startX.toLong(), 0)
        val letters = mutableListOf<Char>()
        val letterCount = input.sumOf { chars -> chars.count { it.isLetter() } }
        var lastDir = Dir.DOWN
        while (letters.size < letterCount) {
            lastDir = input.nextDir(point, lastDir, letters)
            point = lastDir.nextPoint(point)
        }
        return letters.joinToString("")
    }

    override suspend fun solve2(input: Array<CharArray>): Int {
        val startX = input.mapIndexed { index, chars -> index to chars[0] }.first { it.second == '|' }.first
        var point = Point(startX.toLong(), 0)
        val letters = mutableListOf<Char>()
        val letterCount = input.sumOf { chars -> chars.count { it.isLetter() } }
        var lastDir = Dir.DOWN
        var stepCount = 0
        while (letters.size < letterCount) {
            stepCount++
            lastDir = input.nextDir(point, lastDir, letters)
            point = lastDir.nextPoint(point)
        }
        return stepCount
    }

    private fun Array<CharArray>.nextDir(currentPos: Point, lastDir: Dir, letters: MutableList<Char>): Dir {
        val currentSymbol = this[currentPos.x.toInt()][currentPos.y.toInt()]
        if (currentSymbol.isLetter()) letters.add(currentSymbol)
        if (currentSymbol != '+') return lastDir
        val blockedDirection = when (lastDir) {
            Dir.LEFT -> Dir.RIGHT
            Dir.RIGHT -> Dir.LEFT
            Dir.UP -> Dir.DOWN
            Dir.DOWN -> Dir.UP
        }
        val validDirections = Dir.values().filter { it != blockedDirection }
        val newPoints = validDirections.map { it.nextPoint(currentPos) }
        val validPoint = newPoints.first {
            it.x in (0 until 200) && it.y in (0 until 200)
                    && (this[it.x.toInt()][it.y.toInt()].isLetter() || this[it.x.toInt()][it.y.toInt()] in listOf('-', '|', '+'))
        }
        return when {
            validPoint.x < currentPos.x -> Dir.LEFT
            validPoint.x > currentPos.x -> Dir.RIGHT
            validPoint.y < currentPos.y -> Dir.UP
            validPoint.y > currentPos.y -> Dir.DOWN
            else -> error("oh no!")
        }
    }

    private enum class Dir {
        LEFT, RIGHT, UP, DOWN;

        fun nextPoint(point: Point) = when (this) {
            LEFT -> point + Point(-1, 0)
            RIGHT -> point + Point(1, 0)
            UP -> point + Point(0, -1)
            DOWN -> point + Point(0, 1)
        }
    }
}
