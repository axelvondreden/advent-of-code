package y2016

import Day
import Utils
import pathfinding.Pathfinder
import print
import toPathfindingMap

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

    override fun solve2() = 0

    companion object {
        const val mapSize = 100
        val startNode = Pathfinder.Node(1, 1)
        val endNode = Pathfinder.Node(31, 39)
    }
}