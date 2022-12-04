package y2022

import Day

class Day03 : Day(2022, 3) {

    override val input = readStrings()

    override fun solve1() = input.sumOf { line ->
        line.chunked(line.length / 2).let { container ->
            container[0].first { it in container[1] }.priority
        }
    }

    override fun solve2() = input.chunked(3).sumOf { group ->
        group[0].first { it in group[1] && it in group[2] }.priority
    }

    private val Char.priority get() = if (isLowerCase()) code - 96 else code - 38
}