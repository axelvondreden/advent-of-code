package y2016

import Day
import pathfinding.Pathfinder
import utils.permute
import utils.toCharMatrix
import utils.toPathfindingMap
import kotlin.math.min


class Day24 : Day<Array<CharArray>>(2016, 24) {

    override fun List<String>.parse() = toCharMatrix()

    override fun solve1(input: Array<CharArray>): Int {
        val pf = Pathfinder(input.toPathfindingMap(), input.size, input[0].size)
        val points = input.flatMapIndexed { x, chars ->
            chars.mapIndexed { y, c -> if (c.isDigit() && c != '0') Pathfinder.Node(x, y) else null }
        }.filterNotNull()
        val pointPerms = points.permute()
        var minLength = Int.MAX_VALUE
        val start = input.flatMapIndexed { x, chars ->
            chars.mapIndexed { y, c -> if (c == '0') Pathfinder.Node(x, y) else null }
        }.filterNotNull().first()
        pointPerms.forEach { list ->
            var length = pf.searchBFS(start, list.first()).size + 1
            for (i in 0 until list.size - 1) {
                if (length > minLength) continue
                length += pf.searchBFS(list[i], list[i + 1]).size + 1
            }
            minLength = min(minLength, length)
        }
        return minLength
    }

    override fun solve2(input: Array<CharArray>): Int {
        val pf = Pathfinder(input.toPathfindingMap(), input.size, input[0].size)
        val points = input.flatMapIndexed { x, chars ->
            chars.mapIndexed { y, c -> if (c.isDigit() && c != '0') Pathfinder.Node(x, y) else null }
        }.filterNotNull()
        val pointPerms = points.permute()
        var minLength = Int.MAX_VALUE
        val start = input.flatMapIndexed { x, chars ->
            chars.mapIndexed { y, c -> if (c == '0') Pathfinder.Node(x, y) else null }
        }.filterNotNull().first()
        pointPerms.forEach { list ->
            var length = pf.searchBFS(start, list.first()).size + 1
            for (i in 0 until list.size - 1) {
                if (length > minLength) continue
                length += pf.searchBFS(list[i], list[i + 1]).size + 1
            }
            length += pf.searchBFS(list.last(), start).size + 1
            minLength = min(minLength, length)
        }
        return minLength
    }
}