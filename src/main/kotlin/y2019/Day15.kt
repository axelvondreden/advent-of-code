package y2019

import Day
import java.util.*
import kotlin.Comparator
import kotlin.math.abs

class Day15 : Day(2019, 15) {

    override val input = IntCodeComputer(readLongArray())

    private var pathLength = 0
    private var oxyCounter = 0L

    init {
        var location = Pair(0L, 0L)
        var triedLocation: Pair<Long, Long>
        var previousMove: Long
        var oxygenLocation: Pair<Long, Long>? = null
        val map = mutableMapOf(location to 1L)
        val cameFrom = mutableMapOf<Pair<Long, Long>, Pair<Pair<Long, Long>, Long>>()
        var output: Long
        while (true) {
            val neighbors = (1L..4L).map { it to location.move(it) }.filter { it.second !in map }
            if (neighbors.isEmpty()) {
                val target = cameFrom[location]
                if (target == null) {
                    pathLength = findPath(Pair(0L, 0L), oxygenLocation!!, map)!!.size - 1
                    oxyCounter = fillOxygen(oxygenLocation, map)
                    break
                } else {
                    val d = reverseDir(target.second)
                    previousMove = d
                    triedLocation = location.move(d)
                    output = input.addInput(d).run().value
                }
            } else {
                val selected = neighbors.first().first
                triedLocation = location.move(selected)
                previousMove = selected
                output = input.addInput(selected).run().value
            }

            if (triedLocation in map) {
                location = triedLocation
            } else {
                val block = output
                map[triedLocation] = block
                if (block == 2L) {
                    oxygenLocation = triedLocation
                }
                if (block == 1L || block == 2L) {
                    cameFrom[triedLocation] = location to previousMove
                    location = triedLocation
                }
            }
        }
    }

    override fun solve1() = pathLength

    override fun solve2() = oxyCounter

    private fun reconstructPath(cameFrom: Map<Pair<Long, Long>, Pair<Long, Long>>, to: Pair<Long, Long>): List<Pair<Long, Long>> {
        var current = to
        val path = mutableListOf(current)
        while (current in cameFrom) {
            current = cameFrom[current] ?: error("not in map")
            path.add(current)
        }
        return path.reversed()
    }

    private fun neighbors(current: Pair<Long, Long>, map: Map<Pair<Long, Long>, Long>) =
        (1L..4L).map { current.move(it) }.filter { it in map && map[it] != 0L }

    private fun findPath(from: Pair<Long, Long>, target: Pair<Long, Long>, map: Map<Pair<Long, Long>, Long>): List<Pair<Long, Long>>? {
        val openSet = PriorityQueue(Comparator<DistancedPoint> { a, b -> (a.distance - b.distance).toInt() }).apply {
            add(DistancedPoint(from, 0))
        }
        val cameFrom = mutableMapOf<Pair<Long, Long>, Pair<Long, Long>>()
        val gScore = mutableMapOf(from to 0)

        while (!openSet.isEmpty()) {
            val current = openSet.poll().point
            if (current == target) return reconstructPath(cameFrom, current)

            val neighbors = neighbors(current, map)
            neighbors.forEach { n ->
                val score = gScore.getOrDefault(current, Int.MAX_VALUE) + 1
                if (score < gScore.getOrDefault(n, Int.MAX_VALUE)) {
                    cameFrom[n] = current
                    gScore[n] = score
                    openSet.find { it.point == n }?.let { openSet.remove(it) }
                    openSet.add(DistancedPoint(n, score + n.manhattanDistance(target)))
                }
            }
        }
        return null
    }

    private fun fillOxygen(oxygenLocation: Pair<Long, Long>, map: Map<Pair<Long, Long>, Long>): Long {
        val hasOxy = mutableSetOf(oxygenLocation)
        val points = map.keys.filter { (map[it] ?: error("not in map")) == 1L }
        var counter = 0L
        while (points.any { it !in hasOxy }) {
            counter += 1
            val newPoints = hasOxy.flatMap { point ->
                (1L..4L).map { dir -> point.move(dir)}.filter { it in map && (map[it] ?: error("not in map")) == 1L }
            }
            hasOxy.addAll(newPoints)
        }
        return counter
    }

    private fun reverseDir(dir: Long) = when (dir) {
        1L -> 2L
        2L -> 1L
        3L -> 4L
        4L -> 3L
        else -> error("unknown direction")
    }

    private fun Pair<Long, Long>.move(dir: Long) = when (dir) {
        1L -> Pair(first, second - 1)
        2L -> Pair(first, second + 1)
        3L -> Pair(first - 1, second)
        4L -> Pair(first + 1, second)
        else -> error("unknown direction")
    }

    private fun Pair<Long, Long>.manhattanDistance(target: Pair<Long, Long>) = abs(first - target.first) + abs(second - target.second)

    private data class DistancedPoint(val point: Pair<Long, Long>, val distance: Long)
}