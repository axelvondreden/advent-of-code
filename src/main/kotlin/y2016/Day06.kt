package y2016

import Day

class Day06 : Day(2016, 6) {

    override val input = readStrings()

    override fun solve1() = input[0].indices.map { index ->
        input.map { it[index] }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
    }.joinToString("")

    override fun solve2() = input[0].indices.map { index ->
        input.map { it[index] }.groupingBy { it }.eachCount().minByOrNull { it.value }!!.key
    }.joinToString("")
}