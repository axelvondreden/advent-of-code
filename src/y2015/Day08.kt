package y2015

import Day
import Utils

class Day08 : Day() {

    override val input = Utils.readStrings(2015, 8)

    override fun solve1() = input.sumBy {
        it.length - (it.replace(Regex("\\\\\""), "a")
            .replace(Regex("\\\\\\\\"), "a")
            .replace(Regex("\\\\x[0-9a-f]{2}"), "a").length - 2)
    }

    override fun solve2() = input.sumBy {
        it.replace(Regex("\\\\\""), "aaaa")
            .replace(Regex("\\\\\\\\"), "aaaa")
            .replace(Regex("\\\\x[0-9a-f]{2}"), "aaaaa").length + 4 - it.length
    }
}