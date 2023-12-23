package y2023

import Day
import utils.Dir
import utils.Point
import utils.get
import utils.toCharMatrix
import kotlin.math.max

class Day23 : Day<Array<CharArray>>(2023, 23) {

    override suspend fun List<String>.parse() = toCharMatrix()

    override suspend fun solve1(input: Array<CharArray>): Int {
        val start = Point(1, 0)
        val finish = Point(input.size - 2, input[0].size - 1)
        val queue = ArrayDeque<Edge>().apply { add(Edge(start + Dir.UP, start, 0)) }
        var maxDist = -1

        while (queue.isNotEmpty()) {
            val (prev, node, dist) = queue.removeFirst()

            if (node == finish) {
                maxDist = max(maxDist, dist)
                continue
            }

            val newDist = dist + 1
            val directions = when (input[node]) {
                '>' -> listOf(Dir.RIGHT)
                '<' -> listOf(Dir.LEFT)
                '^' -> listOf(Dir.UP)
                'v' -> listOf(Dir.DOWN)
                else -> Dir.entries
            }

            directions
                .map { direction -> node + direction }
                .filter { neighbour -> neighbour != prev && input[neighbour] != '#' }
                .forEach { neighbour -> queue.add(Edge(node, neighbour, newDist)) }
        }

        return maxDist
    }

    override suspend fun solve2(input: Array<CharArray>): Int {
        val start = Point(1, 0)
        val finish = Point(input.size - 2, input[0].size - 1)
        val (nodes, graph) = input.getJunctionGraph(start, finish)
        val queue = ArrayDeque<Triple<Point, Long, Int>>().apply { add(Triple(start, 1L, 0)) }
        var maxDist = -1

        val masks = nodes.withIndex().associate { (index, node) -> node to (1L shl index) }

        while (queue.isNotEmpty()) {
            val (node, path, dist) = queue.removeFirst()

            if (node == finish) {
                maxDist = max(maxDist, dist)
                continue
            }

            graph.filter { (from, till) -> node == from && (masks.getValue(till) and path) == 0L }
                .forEach { (_, till, d) ->
                    queue.add(Triple(till, path or masks.getValue(till), dist + d))
                }
        }

        return maxDist
    }

    private fun Array<CharArray>.getJunctionGraph(start: Point, finish: Point): Pair<List<Point>, List<Edge>> {
        val junctions = flatMapIndexed { y, line ->
            line.indices.mapNotNull { x ->
                Point(x, y).takeIf { p ->
                    p.neighbours().count { isValid(it) && get(it) in "><^v" } > 2
                }
            }
        }

        val nodes = listOf(start) + junctions + listOf(finish)
        val edges = mutableListOf<Edge>()
        val heads = mutableListOf(start)
        val queue = ArrayDeque<Pair<Point, Int>>()

        val visited = mutableSetOf<Point>()

        while (heads.isNotEmpty()) {
            val head = heads.removeFirst()
            if (head == finish) break

            queue.add(head to 0)
            visited.add(head)

            while (queue.isNotEmpty()) {
                val (tail, dist) = queue.removeFirst()

                if (head != tail && tail in nodes) {
                    heads.add(tail)
                    visited.remove(tail)
                    edges.add(Edge(head, tail, dist))
                    edges.add(Edge(tail, head, dist))
                    continue
                }

                tail.neighbours()
                    .filter { isValid(it) && get(it) != '#' && it !in visited }
                    .forEach { neighbour ->
                        visited.add(neighbour)
                        queue.add(neighbour to dist + 1)
                    }
            }
        }

        return nodes to edges
    }

    private fun Array<CharArray>.isValid(point: Point) = point.x in indices && point.y in this[0].indices

    private data class Edge(val from: Point, val till: Point, val dist: Int)
}