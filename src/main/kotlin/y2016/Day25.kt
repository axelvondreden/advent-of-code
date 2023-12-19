package y2016

import Day


class Day25 : Day<List<String>>(2016, 25) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val regex = Regex("^(!?01)+")
        var output = ""
        var value = -1
        while (!regex.matches(output)) {
            output = ""
            value++
            Assembunny(mutableMapOf("a" to value, "b" to 0, "c" to 0, "d" to 0)).run(input.toMutableList()) {
                output += it
                output.length < 10
            }
        }
        return value
    }

    override suspend fun solve2(input: List<String>) = 0
}