package y2023

import Day
import utils.*

class Day10 : Day<List<String>>(2023, 10) {

    private val edges = mapOf(
        '|' to setOf(Dir.UP, Dir.DOWN),
        '-' to setOf(Dir.LEFT, Dir.RIGHT),
        'L' to setOf(Dir.UP, Dir.RIGHT),
        'J' to setOf(Dir.UP, Dir.LEFT),
        '7' to setOf(Dir.DOWN, Dir.LEFT),
        'F' to setOf(Dir.DOWN, Dir.RIGHT),
        '.' to emptySet(),
        'S' to setOf(Dir.UP, Dir.DOWN, Dir.LEFT, Dir.RIGHT)
    )

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val grid = input.flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> Point(x, y) to ch }
        }.toMap()

        val start = grid.filterValues { it == 'S' }.keys.first()

        val direction = Dir.entries.first { it.turn(Turn.AROUND) in grid[start + it]!!.let(edges::getValue) }

        val seq = generateSequence(Pair(start, direction)) { (pos, dir) ->
            val newPos = pos + dir
            val newDir = grid[newPos]!!.let(edges::getValue).first { it != dir.turn(Turn.AROUND) }
            newPos to newDir
        }


        val result = seq.drop(1).indexOfFirst { grid[it.first] == 'S' }

        return (result + 1) / 2
    }

    override suspend fun solve2(input: List<String>): Int {
        val grid = input.flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> Point(x, y) to ch }
        }.toMap()

        val startPosition = grid.filterValues { it == 'S' }.keys.first()
        val connections = Dir.entries.filter { dir -> dir.turn(Turn.AROUND) in (grid[startPosition + dir]?.let { edges[it] } ?: emptyList()) }.toSet()
        val seq = generateSequence(Pair(startPosition, connections.first())) { (pos, dir) ->
            val newPos = pos + dir
            val newDir = edges[grid[newPos]!!]!!.first { it != dir.turn(Turn.AROUND) }
            newPos to newDir
        }
        val pipes = seq.drop(1).takeWhile { grid[it.first] != 'S' }.map { it.first }.toSet()
        val startSymbol = edges.entries.first { it.value == connections }.key
        val pipeMap = grid.filterKeys { it in pipes } + (startPosition to startSymbol)
        val pipeCrossing = edges.filterValues { Dir.DOWN in it }.keys.joinToString("")

        return pipeMap.toGrid().flatMap { row ->
            row.scan(Pair(true, false)) { (_, isInside), ch ->
                Pair(ch == ' ', if (ch in pipeCrossing) !isInside else isInside)
            }
        }.count { (isOpenSpace, isInside) -> isOpenSpace && isInside }
    }

    private fun Map<Point, Char>.yBounds() = (keys.minOf { it.y })..(keys.maxOf { it.y })

    private fun Map<Point, Char>.xBounds() = (keys.minOf { it.x })..(keys.maxOf { it.x })

    private fun Map<Point, Char>.toGrid(): List<List<Char>> {
        val xs = xBounds()
        return yBounds().map { y ->
            xs.map { x ->
                getOrDefault(Point(x, y), ' ')
            }
        }
    }
}