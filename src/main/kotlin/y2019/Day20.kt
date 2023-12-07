package y2019

import Day
import pathfinding.Pathfinder
import utils.toCharMatrix
import utils.toPathfindingMap

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
                            matrix[storedNode.x][storedNode.y] = key
                            matrix[node.x][node.y] = key
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
        matrix[start.x][start.y] = '1'
        matrix[end.x][end.y] = '2'
    }

    private fun neighborsWithPortal(current: Pathfinder.INode): Set<Pathfinder.INode> {
        current as Pathfinder.Node
        val set = mutableSetOf<Pathfinder.INode>(
            Pathfinder.Node(current.x - 1, current.y),
            Pathfinder.Node(current.x + 1, current.y),
            Pathfinder.Node(current.x, current.y - 1),
            Pathfinder.Node(current.x, current.y + 1)
        )
        set.addAll(portals.filter { Pathfinder.Node(it.outsideNode.x, it.outsideNode.y) == current }.map { Pathfinder.Node(it.insideNode.x, it.insideNode.y) })
        set.addAll(portals.filter { Pathfinder.Node(it.insideNode.x, it.insideNode.y) == current }.map { Pathfinder.Node(it.outsideNode.x, it.outsideNode.y) })
        return set
    }

    private fun neighborsWithLvlPortal(current: Pathfinder.INode): Set<Pathfinder.INode> {
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
        return Pathfinder(input.toPathfindingMap(walls), input.size, input[0].size, ::neighborsWithPortal).searchBFS(Pathfinder.Node(start.x, start.y), Pathfinder.Node(end.x, end.y)).size + 1
    }

    override fun solve2(input: Array<CharArray>): Int {
        val walls = ('A'..'Z').toMutableSet().apply { addAll(listOf('#', ' ')) }
        return Pathfinder(input.toPathfindingMap(walls), input.size, input[0].size, ::neighborsWithLvlPortal).searchBFS(start, end).size + 1
    }

    private data class Portal(val c: Char, val outsideNode: LvlNode, val insideNode: LvlNode)

    private class LvlNode(x: Int, y: Int, val lvl: Int): Pathfinder.INode(x, y) {
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
}