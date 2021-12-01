package y2015

import Day

class Day08 : Day(2015, 8) {

    override val input = readStrings()

    override fun solve1() = input.sumOf {
        it.length - (it.replace(Regex("\\\\\""), "a")
            .replace(Regex("\\\\\\\\"), "a")
            .replace(Regex("\\\\x[0-9a-f]{2}"), "a").length - 2)
    }

    override fun solve2() = input.sumOf {
        it.replace(Regex("\\\\\""), "aaaa")
            .replace(Regex("\\\\\\\\"), "aaaa")
            .replace(Regex("\\\\x[0-9a-f]{2}"), "aaaaa").length + 4 - it.length
    }
}