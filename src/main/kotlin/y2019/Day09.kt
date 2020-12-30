package y2019

import Day

class Day09 : Day(2019, 9) {

    override val input = readLongArray()

    override fun solve1() = IntCodeComputer(input.copyOf()).addInput(1L).run().value

    override fun solve2() = IntCodeComputer(input).addInput(2L).run().value
}