package y2023

import Day
import utils.Pathfinder
import utils.*

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

    override fun solve2(input: Array<CharArray>): Int {
        val padded = input.pad('.', 1)
        val start = padded.find('S')!!
        val pipeLocations = mutableSetOf(start)
        var lastPos = start
        var currentPos = padded.findNextFromStart(start)
        while (currentPos != start) {
            pipeLocations += currentPos
            val tempLast = lastPos
            lastPos = currentPos
            currentPos = padded.findNextFromLast(currentPos, tempLast)
        }
        val map = Array(padded.size) { x ->
            CharArray(padded[0].size) { y -> if (Point(x, y) in pipeLocations) '#' else '.' }
        }.toPathfindingMap()
        val pf = Pathfinder(map, padded.size, padded[0].size)
        return padded.indices.sumOf { x ->
            padded[x].indices.count { y ->
                val p = Point(x, y)
                if (p in pipeLocations) {
                    false
                } else {
                    val path = pf.searchBFS(p, Point(0, 0))
                    if (p !in pipeLocations && p.distance(Point(0, 0)) > 1 && path.isEmpty()) {
                        println(p)
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }

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
        else -> error("Big Fail: ${this[current]}")
    }
}