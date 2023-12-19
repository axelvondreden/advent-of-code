package y2019

import Day
import utils.toLongArray

class Day09 : Day<LongArray>(2019, 9) {

    override suspend fun List<String>.parse() = first().toLongArray()

    override suspend fun solve1(input: LongArray) = IntCodeComputer(input.copyOf()).addInput(1L).run().value

    override suspend fun solve2(input: LongArray) = IntCodeComputer(input).addInput(2L).run().value
}