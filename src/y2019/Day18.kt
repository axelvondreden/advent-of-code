package y2019

import AStar
import Day
import find
import toAstar
import java.lang.Integer.min

class Day18: Day() {

    override val input = Utils.readCharMatrix(2019, 18)

    private val keys = ('a'..'z').mapNotNull { input.find(it)?.let { node -> Key(it, node) } }
    private val doors = ('A'..'Z').mapNotNull { input.find(it)?.let { node -> Door(it, node) } }
    private val cache: Map<String, Edge>

    init {
        val map = mutableMapOf<String, Edge>()
        for (start in keys.toMutableList().apply { add(Key('@', input.find('@')!!)) }) {
            for (end in keys) {
                if (start.node != end.node) {
                    val path = AStar(input.toAstar(), input.size, input[0].size).search(start.node, end.node)
                    if (path.isNotEmpty()) {
                        map["${start.c}${end.c}"] = Edge(path.size - 1, doors.filter { it.node in path }.map { it.c }.toCharArray())
                    }
                }
            }
        }
        cache = map
    }

    override fun solve1(): Int {
        val min = intArrayOf(Int.MAX_VALUE)
        val searchKeys = keys.map { it.c }.sortedBy {
            val e = cache["@$it"]!!
            e.length + (e.doors.size * 1000)
        }
        getShortestPath('@', searchKeys, doors.map { it.c }, 0, min, "")
        return min[0]
    }

    override fun solve2() {

    }

    private fun getShortestPath(start: Char, keys: List<Char>, doors: List<Char>, length: Int, minLength: IntArray, id: String) {
        if (keys.isEmpty()) {
            if (length < minLength[0]) {
                minLength[0] = length
                //println("$length $id")
            }
            return
        }
        if (length >= minLength[0]) {
            return
        }
        for (key in keys.subList(0, min(3, keys.size))) {
            val edge = cache["$start$key"]!!
            val newLength = length + edge.length
            val newId = id + key
            if (newLength >= minLength[0]) {
                //if (keys.size == 1) print("$newLength ${newId}\r")
                return
            }
            if (edge.doors.all { it.toLowerCase() in id }) {
                val searchDoors = doors.filter { it != key.toUpperCase() }
                val searchKeys = keys.filter { it != key }.sortedBy {
                    val e = cache["$key$it"]!!
                    e.length + (e.doors.count { door -> door.toLowerCase() !in newId } * 1000)
                }
                getShortestPath(key, searchKeys, searchDoors, newLength, minLength, newId)
            }
        }
    }

    data class Edge(val length: Int, val doors: CharArray)

    data class Door(val c: Char, val node: AStar.Node)

    data class Key(val c: Char, val node: AStar.Node)
}