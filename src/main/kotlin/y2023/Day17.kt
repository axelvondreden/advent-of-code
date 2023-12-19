package y2023

import Day
import utils.Dir
import utils.Point
import utils.Turn
import utils.toIntMatrix
import java.util.*

class Day17 : Day<Array<IntArray>>(2023, 17) {

    override suspend fun List<String>.parse() = toIntMatrix()

    override suspend fun solve1(input: Array<IntArray>): Int {
        return input.solve(minRepeat = 1, maxRepeat = 3)
    }

    override suspend fun solve2(input: Array<IntArray>): Int = input.solve(minRepeat = 4, maxRepeat = 10)

    private fun Array<IntArray>.solve(minRepeat: Int, maxRepeat: Int): Int {
        val finish = Point(lastIndex, this[0].lastIndex)
        val queue = PriorityQueue<Block>(compareBy { it.distance }).apply {
            add(Block(Point(1, 0), Dir.RIGHT, 1, 0))
            add(Block(Point(0, 1), Dir.DOWN, 1, 0))
        }
        val visits = hashMapOf<Triple<Point, Dir, Int>, Int>()

        while (queue.isNotEmpty()) {
            val (node, direction, repeat, distance) = queue.poll()

            val newDistance = distance + weight(node)
            if (node == finish && repeat >= minRepeat) {
                return newDistance
            }

            val visit = Triple(node, direction, repeat)
            if (newDistance >= visits.getOrDefault(visit, Int.MAX_VALUE)) {
                continue
            }

            visits[visit] = newDistance

            if (repeat >= minRepeat) {
                val l = direction.turn(Turn.LEFT)
                val r = direction.turn(Turn.RIGHT)
                (node + l).takeIf { valid(it) }?.let { queue.add(Block(it, l, 1, newDistance)) }
                (node + r).takeIf { valid(it) }?.let { queue.add(Block(it, r, 1, newDistance)) }
            }

            if (repeat < maxRepeat) {
                (node + direction).takeIf { valid(it) }?.let { queue.add(Block(it, direction, repeat + 1, newDistance)) }
            }
        }

        return -1
    }

    private fun Array<IntArray>.weight(p: Point) = this[p.x.toInt()][p.y.toInt()]
    private fun Array<IntArray>.valid(p: Point) = p.x.toInt() in indices && p.y.toInt() in this[0].indices

    private data class Block(
        val point: Point,
        val direction: Dir,
        val repeat: Int,
        val distance: Int,
    )
}