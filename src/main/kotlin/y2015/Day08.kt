package y2015

import Day

class Day08 : Day<List<String>>(2015, 8) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>) = input.sumOf {
        it.length - (it.replace(Regex("\\\\\""), "a")
            .replace(Regex("\\\\\\\\"), "a")
            .replace(Regex("\\\\x[0-9a-f]{2}"), "a").length - 2)
    }

    override suspend fun solve2(input: List<String>) = input.sumOf {
        it.replace(Regex("\\\\\""), "aaaa")
            .replace(Regex("\\\\\\\\"), "aaaa")
            .replace(Regex("\\\\x[0-9a-f]{2}"), "aaaaa").length + 4 - it.length
    }
}