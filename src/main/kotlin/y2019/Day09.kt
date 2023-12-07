package y2019

import Day
import utils.toLongArray

class Day09 : Day<LongArray>(2019, 9) {

    override fun List<String>.parse() = first().toLongArray()

    override fun solve1(input: LongArray) = IntCodeComputer(input.copyOf()).addInput(1L).run().value

    override fun solve2(input: LongArray) = IntCodeComputer(input).addInput(2L).run().value
}