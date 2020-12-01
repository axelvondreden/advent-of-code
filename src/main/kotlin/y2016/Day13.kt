package y2016

import Day
import Utils
import pathfinding.Pathfinder
import toPathfindingMap
import kotlin.math.abs

class Day13 : Day() {

    override val input = Utils.readString(2016, 13).toInt()

    val map = generateMap(mapSize)

    private fun generateMap(size: Int): Array<CharArray> {
        val map = Array(size) { CharArray(size) }
        for (x in 0 until size) {
            for (y in 0 until size) {
                val value = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + input
                map[x][y] = if (value.countOneBits() % 2 == 0) '.' else '#'
            }
        }
        return map
    }

    override fun solve1(): Int {
        val pf = Pathfinder(map.toPathfindingMap(), mapSize, mapSize)
        val path = pf.searchBFS(startNode, endNode)
        return path.size + 1
    }

    override fun solve2(): Int {
        val coords = mutableSetOf(startNode)
        val pf = Pathfinder(map.toPathfindingMap(), mapSize, mapSize)
        for (x in startNode.x - 50 until startNode.x + 51) {
            for (y in startNode.y - 50 until startNode.y + 51) {
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

    companion object {
        const val mapSize = 100
        val startNode = Pathfinder.Node(1, 1)
        val endNode = Pathfinder.Node(31, 39)
    }
}