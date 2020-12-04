package y2019

import Day
import Utils

class Day09 : Day() {

    override val input = Utils.readLongArray(2019, 9)

    override fun solve1() = IntCodeComputer(input.copyOf()).addInput(1L).run().value

    override fun solve2() = IntCodeComputer(input).addInput(2L).run().value
}