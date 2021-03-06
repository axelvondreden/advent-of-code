package y2016

import Day
import pathfinding.Pathfinder
import utils.toPathfindingMap
import kotlin.math.abs

class Day13 : Day(2016, 13) {

    override val input = readString().toInt()

    private val map = Array(mapSize) { x ->
        CharArray(mapSize) { y ->
            if (((x * x) + (3 * x) + (2 * x * y) + y + (y * y) + input).countOneBits() % 2 == 0) '.' else '#'
        }
    }

    override fun solve1(): Int {
        val pf = Pathfinder(map.toPathfindingMap(), mapSize, mapSize)
        val path = pf.searchBFS(startNode, endNode)
        return path.size + 1
    }

    override fun solve2(): Int {
        val coords = mutableSetOf(startNode)
        val pf = Pathfinder(map.toPathfindingMap(), mapSize, mapSize)
        (startNode.x - 50 until startNode.x + 51).forEach { x ->
            (startNode.y - 50 until startNode.y + 51).forEach { y ->
                if (x >= 0 && y >= 0 && map[x][y] == '.') {
                    val endNode = Pathfinder.Node(x, y)
                    val path = pf.searchBFS(startNode, endNode)
                    if (path.isEmpty() && abs(x - startNode.x) + abs(y - startNode.y) <= 1) {
                        coords.add(endNode)
                    }
                    if (path.size in 1..49) {
                        coords.add(endNode)
                    }
                }
            }
        }
        return coords.size
    }

    private companion object {
        private const val mapSize = 100
        private val startNode = Pathfinder.Node(1, 1)
        private val endNode = Pathfinder.Node(31, 39)
    }
}