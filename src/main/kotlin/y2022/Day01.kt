package y2022

import Day

class Day01 : Day<List<List<Int>>>(2022, 1) {

    override fun List<String>.parse() = joinToString(",").split(",,").map { group ->
        group.split(",").map { it.toInt() }
    }

    override fun solve1(input: List<List<Int>>) = input.maxOf { it.sum() }

    override fun solve2(input: List<List<Int>>) = input.map { it.sum() }.sortedDescending().take(3).sum()
}