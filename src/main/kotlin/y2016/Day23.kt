package y2016

import Day


class Day23 : Day<List<String>>(2016, 23) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val register = mutableMapOf("a" to 7, "b" to 0, "c" to 0, "d" to 0)
        Assembunny(register).run(input.toMutableList())
        return register["a"]!!
    }

    override suspend fun solve2(input: List<String>): Int {
        val register = mutableMapOf("a" to 12, "b" to 0, "c" to 0, "d" to 0)
        Assembunny(register).run(input.toMutableList())
        return register["a"]!!
    }
}