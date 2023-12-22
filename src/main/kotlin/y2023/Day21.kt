package y2023

import Day
import utils.*

class Day21 : Day<Array<CharArray>>(2023, 21) {

    override suspend fun List<String>.parse() = toCharMatrix()

    override suspend fun solve1(input: Array<CharArray>): Int {
        val start = input.find('S')!!
        return input.visitedPointsMap(start).filterValues { 64 >= it && it % 2 == 0 }.size
    }

    override suspend fun solve2(input: Array<CharArray>): Long {
        val points = input.visitedPointsMap(input.find('S')!!)

        val n = ((26501365 - (input.size / 2)) / input.size).toLong()
        require(n == 202_300L)

        val oddFull = points.filterValues { it % 2 == 1 }.size
        val oddCorners = points.filterValues { it > 65 && it % 2 == 1 }.size

        val evenFull = points.filterValues { it % 2 == 0 }.size
        val evenCorners = points.filterValues { it > 65 && it % 2 == 0 }.size

        return (n + 1) * (n + 1) * oddFull + n * n * evenFull - ((n + 1) * oddCorners) + (n * evenCorners)
    }

    private fun Array<CharArray>.visitedPointsMap(start: Point): Map<Point, Int> {
        val queue = ArrayDeque<Pair<Point, Int>>().apply { add(start to 0) }
        val visited = hashMapOf<Point, Int>()

        while (queue.isNotEmpty()) {
            val (point, step) = queue.removeFirst()

            val nextStep = step + 1

            point.neighbours()
                .filter { valid(it) && it !in visited }
                .forEach { neighbour ->
                    visited[neighbour] = nextStep
                    queue.add(neighbour to nextStep)
                }
        }

        return visited
    }

    private fun Array<CharArray>.valid(point: Point) = point.x in indices && point.y in this[0].indices && this[point] != '#'
}