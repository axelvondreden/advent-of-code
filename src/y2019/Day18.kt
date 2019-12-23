package y2019

import pathfinding.Pathfinder
import Day
import Utils
import find
import toPathfiningMap

class Day18: Day() {

    override val input = Utils.readCharMatrix(2019, 18)

    private val keys = ('a'..'z').mapNotNull { input.find(it)?.let { node -> Key(it, node) } }
    private val doors = ('A'..'Z').mapNotNull { input.find(it)?.let { node -> Door(it, node) } }
    private val cache1: Map<String, Edge>
    private val cache2: Map<String, Edge>

    init {
        cache1 = getCache(keys.toMutableList().apply { add(Key('@', input.find('@')!!)) })

        val midX = input.indices.last / 2
        val midY = input[0].indices.last / 2
        input.apply {
            get(midX - 1)[midY - 1] = '1'; get(midX)[midY - 1] = '#'; get(midX + 1)[midY - 1] = '2'
            get(midX - 1)[midY] = '#'; get(midX)[midY] = '#'; get(midX + 1)[midY] = '#'
            get(midX - 1)[midY + 1] = '3'; get(midX)[midY + 1] = '#'; get(midX + 1)[midY + 1] = '4'
        }

        cache2 = getCache(keys.toMutableList().apply {
            add(Key('1', input.find('1')!!))
            add(Key('2', input.find('2')!!))
            add(Key('3', input.find('3')!!))
            add(Key('4', input.find('4')!!))
        })
    }

    private fun getCache(keysWithRobot: List<Key>): MutableMap<String, Edge> {
        val map = mutableMapOf<String, Edge>()
        for (start in keysWithRobot) {
            for (end in keys) {
                if (start.node != end.node) {
                    val path = Pathfinder(input.toPathfiningMap(), input.size, input[0].size)
                        .searchAStar(start.node, end.node)
                    if (path.isNotEmpty()) {
                        val k = keys.filter { it.c != start.c && it.c != end.c && it.node in path }.map { it.c }.toCharArray()
                        map["${start.c}${end.c}"] = Edge(path.size - 1, doors.filter { it.node in path }.map { it.c }.toCharArray(), k)
                    }
                }
            }
        }
        return map
    }

    override fun solve1(): Int {
        val min = intArrayOf(Int.MAX_VALUE)
        getShortestPath('@', keys.map { it.c }, doors.map { it.c }, 0, min, "")
        return min[0]
    }

    override fun solve2(): Int {
        val min = intArrayOf(Int.MAX_VALUE)
        getShortestPath(listOf('1', '2', '3', '4'), keys.map { it.c }, doors.map { it.c }, 0, min, "")
        return min[0]
    }

    private fun getShortestPath(start: Char, keys: List<Char>, doors: List<Char>, length: Int, minLength: IntArray, id: String) {
        if (keys.isEmpty()) {
            if (length < minLength[0]) {
                minLength[0] = length
                //println("$length $id")
            }
            return
        }
        for (key in keys) {
            val edge = cache1["$start$key"]!!
            val newLength = length + edge.length
            val newId = id + key
            if (newLength >= minLength[0]) {
                //if (keys.size == 1) print("$newLength ${newId}\r")
                return
            }
            if (edge.doors.all { it.toLowerCase() in id } && edge.keys.none { it in keys }) {
                val searchDoors = doors.filter { it != key.toUpperCase() }
                getShortestPath(key,  keys.filter { it != key }, searchDoors, newLength, minLength, newId)
            }
        }
    }

    private fun getShortestPath(start: List<Char>, keys: List<Char>, doors: List<Char>, length: Int, minLength: IntArray, id: String) {
        if (keys.isEmpty()) {
            if (length < minLength[0]) {
                minLength[0] = length
                //println("$length $id")
            }
            return
        }
        for (key in keys) {
            for (st in start) {
                val edge = cache2["$st$key"] ?: continue
                val newLength = length + edge.length
                val newId = id + key
                if (newLength >= minLength[0]) {
                    //if (keys.size == 1) print("$newLength ${newId}\r")
                    continue
                }
                if (edge.doors.all { it.toLowerCase() in id } && edge.keys.none { it in keys }) {
                    val searchDoors = doors.filter { it != key.toUpperCase() }
                    getShortestPath(start.toMutableList().apply { remove(st); add(key) }, keys.filter { it != key }, searchDoors, newLength, minLength, newId)
                }
            }
        }
    }

    data class Edge(val length: Int, val doors: CharArray, val keys: CharArray)

    data class Door(val c: Char, val node: Pathfinder.Node)

    data class Key(val c: Char, val node: Pathfinder.Node)
}