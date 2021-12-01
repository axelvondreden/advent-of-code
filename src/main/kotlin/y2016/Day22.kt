package y2016

import Day
import kotlin.math.abs


class Day22 : Day(2016, 22) {

    override val input = readStrings().drop(2).map {
        val split = it.split(Regex("\\s+"))
        val positionSplit = split[0].split("-")
        val x = positionSplit[1].drop(1).toInt()
        val y = positionSplit[2].drop(1).toInt()
        Node(x, y, split[1].dropLast(1).toInt(), split[2].dropLast(1).toInt())
    }

    override fun solve1() = input.sumOf { nodeA ->
        input.count { nodeB -> nodeA != nodeB && nodeA.used > 0 && nodeA.used <= nodeB.size - nodeB.used }
    }

    override fun solve2(): Int {
        val xMax = input.maxByOrNull { it.x }!!.x
        val wall = input.filter { it.size > 250 }.minByOrNull { it.x }!!
        val empty = input.first { it.used == 0 }
        return abs(empty.x - wall.x) + 1 + empty.y + (xMax - wall.x) + (5 * xMax.dec()) + 1
    }

    data class Node(val x: Int, val y: Int, val size: Int, var used: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Node
            if (x != other.x) return false
            if (y != other.y) return false
            return true
        }

        override fun hashCode() = 31 * x + y
    }
}