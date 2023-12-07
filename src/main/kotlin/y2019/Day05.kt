package y2019

import Day
import utils.toLongArray

class Day05 : Day<LongArray>(2019, 5) {

    override fun List<String>.parse() = first().toLongArray()

    override fun solve1(input: LongArray) = IntCodeComputer(input.copyOf(), outputZeroes = false).withInputs(longArrayOf(1)).run().value

    override fun solve2(input: LongArray) = IntCodeComputer(input).withInputs(longArrayOf(5)).run().value
}