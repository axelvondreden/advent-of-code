package y2017

import Day
import utils.IO

class Day04 : Day() {

    override val input = IO.readStrings(2017, 4)

    override fun solve1() = input.count {
        val split = it.split(" ")
        split.size == split.distinct().size
    }

    override fun solve2() = input.count { line ->
        val split = line.split(" ").map { it.toCharArray().sorted().joinToString("") }
        split.size == split.distinct().size
    }
}