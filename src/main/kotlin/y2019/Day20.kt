package y2019

import Day
import utils.*

class Day20 : Day<Array<CharArray>>(2019, 20) {

    private lateinit var start: LvlNode
    private lateinit var end: LvlNode

    private lateinit var portals: Set<Portal>

    override fun List<String>.parse() = toCharMatrix().also { matrix ->
        var key = 'a'
        val storage = mutableMapOf<String, LvlNode>()
        val tempPortals = mutableSetOf<Portal>()
        matrix[0].indices.forEach { y ->
            matrix.indices.forEach { x ->
                if (matrix[x][y] in 'A'..'Z') {
                    var str = matrix[x][y].toString()
                    var node: LvlNode? = null
                    if (x + 1 < matrix.size && matrix[x + 1][y] in 'A'..'Z') {
                        str += matrix[x + 1][y]
                        node = (if (x + 2 < matrix.size && matrix[x + 2][y] == '.') LvlNode(x + 2, y, 0) else LvlNode(x - 1, y, 0))
                    } else if (y + 1 < matrix[0].size && matrix[x][y + 1] in 'A'..'Z') {
                        str += matrix[x][y + 1]
                        node = (if (y + 2 < matrix[0].size && matrix[x][y + 2] == '.') LvlNode(x, y + 2, 0) else LvlNode(x, y - 1, 0))
                    }
                    if (node != null) {
                        if (storage.containsKey(str)) {
                            val storedNode = storage.remove(str)!!
                            if (storedNode.x < 3 || storedNode.y < 3 || storedNode.x > matrix.size - 3 || storedNode.y > matrix[0].size - 3) {
                                tempPortals.add(Portal(key, storedNode, node))
                            } else {
                                tempPortals.add(Portal(key, node, storedNode))
                            }
                            matrix[storedNode] = key
                            matrix[node] = key
                            key++
                        } else {
                            storage[str] = node
                        }
                    }
                }
            }
        }

        portals = tempPortals
        start = storage["AA"]!!
        end = storage["ZZ"]!!
        matrix[start] = '1'
        matrix[end] = '2'
    }

    private fun neighborsWithPortal(current: PFNode): Set<PFNode> {
        current as Point
        val set = mutableSetOf<PFNode>(
            Point(current.x - 1, current.y),
            Point(current.x + 1, current.y),
            Point(current.x, current.y - 1),
            Point(current.x, current.y + 1)
        )
        set.addAll(portals.filter { Point(it.outsideNode.x, it.outsideNode.y) == current }.map { Point(it.insideNode.x, it.insideNode.y) })
        set.addAll(portals.filter { Point(it.insideNode.x, it.insideNode.y) == current }.map { Point(it.outsideNode.x, it.outsideNode.y) })
        return set
    }

    private fun neighborsWithLvlPortal(current: PFNode): Set<PFNode> {
        current as LvlNode
        val set = mutableSetOf(
            LvlNode(current.x - 1, current.y, current.lvl),
            LvlNode(current.x + 1, current.y, current.lvl),
            LvlNode(current.x, current.y - 1, current.lvl),
            LvlNode(current.x, current.y + 1, current.lvl)
        )
        set.addAll(portals.filter { current.lvl != 0 && it.outsideNode.x == current.x && it.outsideNode.y == current.y }.map { LvlNode(it.insideNode.x, it.insideNode.y, current.lvl - 1) })
        set.addAll(portals.filter { it.insideNode.x == current.x && it.insideNode.y == current.y }.map { LvlNode(it.outsideNode.x, it.outsideNode.y, current.lvl + 1) })
        return set
    }

    override fun solve1(input: Array<CharArray>): Int {
        val walls = ('A'..'Z').toMutableSet().apply { addAll(listOf('#', ' ')) }
        return Pathfinder(input.toPathfindingMap(walls), input.size, input[0].size, ::neighborsWithPortal).searchBFS(Point(start.x, start.y), Point(end.x, end.y)).size + 1
    }

    override fun solve2(input: Array<CharArray>): Int {
        val walls = ('A'..'Z').toMutableSet().apply { addAll(listOf('#', ' ')) }
        return Pathfinder(input.toPathfindingMap(walls), input.size, input[0].size, ::neighborsWithLvlPortal).searchBFS(start, end).size + 1
    }

    private data class Portal(val c: Char, val outsideNode: LvlNode, val insideNode: LvlNode)

    private class LvlNode(x: Long, y: Long, val lvl: Int): PFNode(x, y) {
        constructor(x: Int, y: Int, lvl: Int) : this(x.toLong(), y.toLong(), lvl)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as LvlNode
            return lvl == other.lvl
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + lvl
            return result
        }
    }

    private operator fun Array<CharArray>.get(point: LvlNode) = this[point.x.toInt()][point.y.toInt()]

    private operator fun Array<CharArray>.set(point: LvlNode, value: Char) {
        this[point.x.toInt()][point.y.toInt()] = value
    }
}