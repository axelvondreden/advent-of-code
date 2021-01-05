package y2019

import Day

class Day05 : Day(2019, 5) {

    override val input = readLongArray()

    override fun solve1() = IntCodeComputer(input.copyOf(), outputZeroes = false).withInputs(longArrayOf(1)).run().value

    override fun solve2() = IntCodeComputer(input).withInputs(longArrayOf(5)).run().value
}