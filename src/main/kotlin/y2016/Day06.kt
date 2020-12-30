package y2016

import Day

class Day06 : Day(2016, 6) {

    override val input = readStrings()

    override fun solve1(): String {
        var ret = ""
        for (element in input[0]) {
            ret += input.map { element }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
        }
        return ret
    }

    override fun solve2(): String {
        var ret = ""
        for (element in input[0]) {
            ret += input.map { element }.groupingBy { it }.eachCount().minByOrNull { it.value }!!.key
        }
        return ret
    }
}