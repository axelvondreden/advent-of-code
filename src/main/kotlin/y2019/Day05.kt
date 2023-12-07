package y2019

import Day

class Day05 : Day<List<String>>(2019, 5) {

    override val input = readLongArray()

    override fun solve1(input: List<String>) = IntCodeComputer(input.copyOf(), outputZeroes = false).withInputs(longArrayOf(1)).run().value

    override fun solve2(input: List<String>) = IntCodeComputer(input).withInputs(longArrayOf(5)).run().value
}