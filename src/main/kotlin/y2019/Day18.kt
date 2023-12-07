package y2019

import Day
import pathfinding.Pathfinder
import utils.find
import utils.toPathfindingMap

class Day18: Day<Any?>(2019, 18) {

    override val input = readCharMatrix()

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
        keysWithRobot.forEach { start ->
            keys.forEach { end ->
                if (start.node != end.node) {
                    val path = Pathfinder(input.toPathfindingMap(), input.size, input[0].size).searchAStar(start.node, end.node)
                    if (path.isNotEmpty()) {
                        val k = keys.filter { it.c != start.c && it.c != end.c && it.node in path }.map { it.c }.toCharArray()
                        map["${start.c}${end.c}"] = Edge(path.size - 1, doors.filter { it.node in path }.map { it.c }.toCharArray(), k)
                    }
                }
            }
        }
        return map
    }

    override fun solve1(input: List<String>): Int {
        val min = intArrayOf(Int.MAX_VALUE)
        getShortestPath('@', keys.map { it.c }, doors.map { it.c }, 0, min, "")
        return min[0]
    }

    override fun solve2(input: List<String>): Int {
        val min = intArrayOf(Int.MAX_VALUE)
        getShortestPath(listOf('1', '2', '3', '4'), keys.map { it.c }, doors.map { it.c }, 0, min, "")
        return min[0]
    }

    private fun getShortestPath(start: Char, keys: List<Char>, doors: List<Char>, length: Int, minLength: IntArray, id: String) {
        if (keys.isEmpty()) {
            if (length < minLength[0]) minLength[0] = length
            return
        }
        keys.forEach { key ->
            val edge = cache1.getValue("$start$key")
            val newLength = length + edge.length
            val newId = id + key
            if (newLength >= minLength[0]) return
            if (edge.doors.all { it.lowercaseChar() in id } && edge.keys.none { it in keys }) {
                val searchDoors = doors.filter { it != key.uppercaseChar() }
                getShortestPath(key,  keys.filter { it != key }, searchDoors, newLength, minLength, newId)
            }
        }
    }

    private fun getShortestPath(start: List<Char>, keys: List<Char>, doors: List<Char>, length: Int, minLength: IntArray, id: String) {
        if (keys.isEmpty()) {
            if (length < minLength[0]) minLength[0] = length
            return
        }
        keys.forEach { key ->
            for (st in start) {
                val edge = cache2["$st$key"] ?: continue
                val newLength = length + edge.length
                val newId = id + key
                if (newLength >= minLength[0]) continue
                if (edge.doors.all { it.lowercaseChar() in id } && edge.keys.none { it in keys }) {
                    val searchDoors = doors.filter { it != key.uppercaseChar() }
                    getShortestPath(start.toMutableList().apply { remove(st); add(key) }, keys.filter { it != key }, searchDoors, newLength, minLength, newId)
                }
            }
        }
    }

    private data class Edge(val length: Int, val doors: CharArray, val keys: CharArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Edge
            if (length != other.length) return false
            if (!doors.contentEquals(other.doors)) return false
            if (!keys.contentEquals(other.keys)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = length
            result = 31 * result + doors.contentHashCode()
            result = 31 * result + keys.contentHashCode()
            return result
        }
    }

    private data class Door(val c: Char, val node: Pathfinder.Node)

    private data class Key(val c: Char, val node: Pathfinder.Node)
}