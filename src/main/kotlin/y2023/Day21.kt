package y2023

import Day
import utils.*

class Day21 : Day<Array<CharArray>>(2023, 21) {

    override suspend fun List<String>.parse() = toCharMatrix()

    override suspend fun solve1(input: Array<CharArray>): Int {
        val pf = Pathfinder(input.toPathfindingMap(), input.size, input[0].size)
        val start = input.find('S')!!
        var count = 0
        for (x in input.indices) {
            for (y in input[0].indices) {
                if (input[x][y] != '#') {
                    val point = Point(x, y)
                    if (point != start && point.distance(start) <= 65) {
                        val path = pf.searchAStar(start, point)
                        if (path.size <= 63) {
                            println("$x $y")
                            count++
                        }
                    }
                }
            }
        }
        return count
    }

    override suspend fun solve2(input: Array<CharArray>): Int {
        return 0
    }
}