package y2016

import Day
import Utils

class Day06 : Day() {

    override val input = Utils.readStrings(2016, 6)

    override fun solve1(): String {
        var ret = ""
        for (i in 0 until input[0].length) {
            ret += input.map { it[i] }.groupingBy { it }.eachCount().maxBy { it.value }!!.key
        }
        return ret
    }

    override fun solve2(): String {
        var ret = ""
        for (i in 0 until input[0].length) {
            ret += input.map { it[i] }.groupingBy { it }.eachCount().minBy { it.value }!!.key
        }
        return ret
    }
}