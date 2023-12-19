package y2016

import Day

class Day06 : Day<List<String>>(2016, 6) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>) = input[0].indices.map { index ->
        input.map { it[index] }.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
    }.joinToString("")

    override suspend fun solve2(input: List<String>) = input[0].indices.map { index ->
        input.map { it[index] }.groupingBy { it }.eachCount().minByOrNull { it.value }!!.key
    }.joinToString("")
}