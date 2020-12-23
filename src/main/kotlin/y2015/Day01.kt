package y2015

import Day
import utils.IO

class Day01 : Day() {

    override val input = IO.readString(2015, 1)

    override fun solve1() = input.count { it == '(' } - input.count { it == ')' }

    override fun solve2(): Int {
        var floor = 0
        for ((index, c) in input.withIndex()) {
            when (c) {
                '(' -> floor++
                ')' -> floor--
            }
            if (floor < 0) {
                return index + 1
            }
        }
        return 0
    }
}