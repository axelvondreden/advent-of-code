package y2019

import Day
import utils.IO
import pathfinding.Pathfinder
import utils.toPathfindingMap

class Day20 : Day() {

    override val input = IO.readCharMatrix(2019, 20)

    private val start: LvlNode
    private val end: LvlNode

    private val portals: Set<Portal>

    init {
        var key = 'a'
        val storage = mutableMapOf<String, LvlNode>()
        val tempPortals = mutableSetOf<Portal>()
        for (y in input[0].indices) {
            for (x in input.indices) {
                if (input[x][y] in 'A'..'Z') {
                    var str = input[x][y].toString()
                    var node: LvlNode? = null
                    if (x + 1 < input.size && input[x + 1][y] in 'A'..'Z') {
                        str += input[x + 1][y]
                        node = (if (x + 2 < input.size && input[x + 2][y] == '.') LvlNode(x + 2, y, 0) else LvlNode(x - 1, y, 0))
                    } else if (y + 1 < input[0].size && input[x][y + 1] in 'A'..'Z') {
                        str += input[x][y + 1]
                        node = (if (y + 2 < input[0].size && input[x][y + 2] == '.') LvlNode(x, y + 2, 0) else LvlNode(x, y - 1, 0))
                    }
                    if (node != null) {
                        if (storage.containsKey(str)) {
                            val storedNode = storage.remove(str)!!
                            if (storedNode.x < 3 || storedNode.y < 3 || storedNode.x > input.size - 3 || storedNode.y > input[0].size - 3) {
                                tempPortals.add(Portal(key, storedNode, node))
                            } else {
                                tempPortals.add(Portal(key, node, storedNode))
                            }
                            input[storedNode.x][storedNode.y] = key
                            input[node.x][node.y] = key
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
        input[start.x][start.y] = '1'
        input[end.x][end.y] = '2'
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

    override fun solve1(): Int {
        val walls = ('A'..'Z').toMutableSet().apply { addAll(listOf('#', ' ')) }
        return Pathfinder(input.toPathfindingMap(walls), input.size, input[0].size, ::neighborsWithPortal).searchBFS(Pathfinder.Node(start.x, start.y), Pathfinder.Node(end.x, end.y)).size + 1
    }

    override fun solve2(): Int {
        val walls = ('A'..'Z').toMutableSet().apply { addAll(listOf('#', ' ')) }
        return Pathfinder(input.toPathfindingMap(walls), input.size, input[0].size, ::neighborsWithLvlPortal).searchBFS(start, end).size + 1
    }

    data class Portal(val c: Char, val outsideNode: LvlNode, val insideNode: LvlNode)

    class LvlNode(x: Int, y: Int, val lvl: Int): Pathfinder.INode(x, y) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as LvlNode

            if (lvl != other.lvl) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + lvl
            return result
        }
    }
}