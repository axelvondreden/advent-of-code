package y2022

import Day

class Day06 : Day<List<String>>(2022, 6) {

    override fun List<String>.parse() = first()

    override fun solve1(input: List<String>) = input.findMarker(4)

    override fun solve2(input: List<String>) = input.findMarker(14)

    private fun String.findMarker(size: Int): Int = windowed(size, 1).indexOfFirst { it.toSet().size == size } + size
}