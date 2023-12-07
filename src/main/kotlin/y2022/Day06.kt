package y2022

import Day

class Day06 : Day<Any?>(2022, 6) {

    override val input = readString()

    override fun solve1(input: List<String>) = input.findMarker(4)

    override fun solve2(input: List<String>) = input.findMarker(14)

    private fun String.findMarker(size: Int): Int = windowed(size, 1).indexOfFirst { it.toSet().size == size } + size
}