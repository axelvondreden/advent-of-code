package y2015

import Day

class Day01 : Day<String>(2015, 1) {

    override fun List<String>.parse() = this[0]

    override fun solve1(input: String) = input.count { it == '(' } - input.count { it == ')' }

    override fun solve2(input: String): Int {
        var floor = 0
        for ((index, c) in input.withIndex()) {
            when (c) {
                '(' -> floor++
                ')' -> floor--
            }
            if (floor < 0) return index + 1
        }
        return 0
    }
}