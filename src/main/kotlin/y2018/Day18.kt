package y2018

import Day
import utils.Point

class Day18 : Day<List<String>>(2018, 18) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>) = value(input.states().drop(10).first())

    override suspend fun solve2(input: List<String>): Int {
        val seen = mutableSetOf<List<String>>()
        val firstRepeater = input.states().find { !seen.add(it) }
        val firstSeen = input.states().indexOfFirst { it == firstRepeater }
        val period = input.states().drop(firstSeen + 1).indexOfFirst { it == firstRepeater }
        val leftover = (1000000000 - firstSeen) % (period + 1)
        return value(input.states().drop(firstSeen + leftover).first())
    }

    private fun insideGrid(point: Point, state: List<String>): Boolean = with(point) {
        state.indices.contains(y) && (0 until state[0].length).contains(x)
    }

    private fun charAt(point: Point, state: List<String>): Char = with(point) { state[y.toInt()][x.toInt()] }

    private fun neighbors(point: Point, state: List<String>): List<Char> = listOf(
        point up 1 left 1,
        point up 1,
        point up 1 right 1,
        point right 1,
        point right 1 down 1,
        point down 1,
        point down 1 left 1,
        point left 1
    ).filter { insideGrid(it, state) }.map { charAt(it, state) }

    private fun nextChar(char: Char, point: Point, state: List<String>): Char {
        val neighborChars = neighbors(point, state).groupingBy { it }.eachCount()
        return when (char) {
            '.' -> when {
                neighborChars.getOrDefault('|', 0) >= 3 -> '|'
                else -> '.'
            }

            '|' -> when {
                neighborChars.getOrDefault('#', 0) >= 3 -> '#'
                else -> '|'
            }

            '#' -> when {
                neighborChars.contains('#') && neighborChars.contains('|') -> '#'
                else -> '.'
            }

            else -> throw IllegalStateException()
        }
    }

    private fun List<String>.states() = sequence {
        var state = this@states
        while (true) {
            yield(state)
            state = state.mapIndexed { y, line ->
                line.toList().mapIndexed { x, char ->
                    nextChar(char, Point(x, y), state)
                }.joinToString("")
            }
        }
    }


    fun value(state: List<String>): Int {
        val characters = state.map { it.toList() }.flatten().groupingBy { it }.eachCount()
        return characters['#']!! * characters['|']!!
    }
}