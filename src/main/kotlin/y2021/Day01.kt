package y2021

import Day

class Day01 : Day<List<String>>(2021, 1) {

    override val input = readInts()

    override fun solve1(input: List<String>) = input.countIncreases()

    override fun solve2(input: List<String>) = (0..input.lastIndex - 2).map { input[it] + input[it + 1] + input[it + 2] }.countIncreases()

    private fun List<Int>.countIncreases() = zipWithNext { a, b -> if (b > a) 1 else 0 }.sum()
}