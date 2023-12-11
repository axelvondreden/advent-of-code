package y2023

import Day
import utils.Point
import utils.findPoints
import utils.toCharMatrix
import utils.get

class Day10 : Day<Array<CharArray>>(2023, 10) {

    override fun List<String>.parse() = toCharMatrix()

    override fun solve1(input: Array<CharArray>): Int {
        val start = input.findPoints('S').first()
        var lastPos = start
        var currentPos = input.findNextFromStart(start)
        var steps = 1
        while (currentPos != start) {
            val tempLast = lastPos
            lastPos = currentPos
            currentPos = input.findNextFromLast(currentPos, tempLast)
            steps++
        }
        return steps / 2
    }

    override fun solve2(input: Array<CharArray>): Int = 0

    private fun Array<CharArray>.findNextFromStart(start: Point) = when {
        start.x > 0 && this[start left 1] in listOf('-', 'L', 'F') -> start left 1
        start.x < lastIndex && this[start right 1] in listOf('-', 'J', '7') -> start right 1
        start.y > 0 && this[start up 1] in listOf('|', 'F', '7') -> start up 1
        start.y < this[0].lastIndex && this[start down 1] in listOf('|', 'L', 'J') -> start down 1
        else -> error("Big Fail")
    }

    private fun Array<CharArray>.findNextFromLast(current: Point, last: Point) = when (this[current]) {
        '-' -> if (last == current left 1) current right 1 else current left 1
        '|' -> if (last == current up 1) current down 1 else current up 1
        'J' -> if (last == current left 1) current up 1 else current left 1
        'L' -> if (last == current right 1) current up 1 else current right 1
        '7' -> if (last == current left 1) current down 1 else current left 1
        'F' -> if (last == current right 1) current down 1 else current right 1
        else -> error("Big Fail")
    }
}