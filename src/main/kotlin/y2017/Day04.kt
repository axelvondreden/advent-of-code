package y2017

import Day

class Day04 : Day<List<String>>(2017, 4) {

    override val input = readStrings()

    override fun solve1(input: List<String>) = input.count {
        val split = it.split(" ")
        split.size == split.distinct().size
    }

    override fun solve2(input: List<String>) = input.count { line ->
        val split = line.split(" ").map { it.toCharArray().sorted().joinToString("") }
        split.size == split.distinct().size
    }
}