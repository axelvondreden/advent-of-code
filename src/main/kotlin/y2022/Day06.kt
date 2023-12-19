package y2022

import Day

class Day06 : Day<String>(2022, 6) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String) = input.findMarker(4)

    override suspend fun solve2(input: String) = input.findMarker(14)

    private fun String.findMarker(size: Int): Int = windowed(size, 1).indexOfFirst { it.toSet().size == size } + size
}