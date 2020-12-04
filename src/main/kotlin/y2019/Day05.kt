package y2019

import Day
import Utils

class Day05 : Day() {

    override val input = Utils.readLongArray(2019, 5)

    override fun solve1() = IntCodeComputer(input.copyOf()).withInputs(longArrayOf(1)).run().value

    override fun solve2() = IntCodeComputer(input).withInputs(longArrayOf(5)).run().value
}