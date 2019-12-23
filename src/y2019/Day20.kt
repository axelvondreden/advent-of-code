package y2019

import Day
import Utils
import pathfinding.Pathfinder
import print
import toPathfiningMap

class Day20 : Day() {

    override val input = Utils.readCharMatrix(2019, 20)

    private val start: Pathfinder.Node
    private val end: Pathfinder.Node

    private val portals: Set<Portal>

    init {
        var key = 'a'
        val storage = mutableMapOf<String, Pathfinder.Node>()
        val tempPortals = mutableSetOf<Portal>()
        for (y in input[0].indices) {
            for (x in input.indices) {
                if (input[x][y] in 'A'..'Z') {
                    var str = input[x][y].toString()
                    var node: Pathfinder.Node? = null
                    if (x + 1 < input.size && input[x + 1][y] in 'A'..'Z') {
                        str += input[x + 1][y]
                        node = (if (x + 2 < input.size && input[x + 2][y] == '.') Pathfinder.Node(
                            x + 2,
                            y
                        ) else Pathfinder.Node(x - 1, y))
                    } else if (y + 1 < input[0].size && input[x][y + 1] in 'A'..'Z') {
                        str += input[x][y + 1]
                        node = (if (y + 2 < input[0].size && input[x][y + 2] == '.') Pathfinder.Node(
                            x,
                            y + 2
                        ) else Pathfinder.Node(x, y - 1))
                    }
                    if (node != null) {
                        if (storage.containsKey(str)) {
                            val storedNode = storage.remove(str)!!
                            tempPortals.add(Portal(key, storedNode, node))
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

    private fun neighborsWithPortal(current: Pathfinder.Node): Set<Pathfinder.Node> {
        val set = mutableSetOf(
            Pathfinder.Node(current.x - 1, current.y),
            Pathfinder.Node(current.x + 1, current.y),
            Pathfinder.Node(current.x, current.y - 1),
            Pathfinder.Node(current.x, current.y + 1)
        )
        set.addAll(portals.filter { it.node1 == current }.map { it.node2 })
        set.addAll(portals.filter { it.node2 == current }.map { it.node1 })
        return set
    }

    override fun solve1(): Int {
        val walls = ('A'..'Z').toMutableSet().apply { addAll(listOf('#', ' ')) }
        return Pathfinder(input.toPathfiningMap(walls), input.size, input[0].size, ::neighborsWithPortal).searchBFS(start, end).size + 1
    }

    override fun solve2(): Int {
        return 0
    }

    data class Portal(val c: Char, val node1: Pathfinder.Node, val node2: Pathfinder.Node)
}