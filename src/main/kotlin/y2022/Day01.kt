package y2022

import Day

class Day01 : Day<List<String>>(2022, 1) {

    override val input = readStrings().joinToString(",").split(",,").map { group ->
        group.split(",").map { it.toInt() }
    }

    override fun solve1(input: List<String>) = input.maxOf { it.sum() }

    override fun solve2(input: List<String>) = input.map { it.sum() }.sortedDescending().take(3).sum()
}