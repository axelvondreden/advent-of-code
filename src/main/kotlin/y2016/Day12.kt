package y2016

import Day

class Day12 : Day<List<String>>(2016, 12) {

    override fun List<String>.parse() = this

    private val register = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)

    override fun solve1(input: List<String>): Int {
        val register = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)
        Assembunny(register).run(input.toMutableList())
        return register["a"]!!
    }

    override fun solve2(input: List<String>): Int {
        val register = mutableMapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0)
        Assembunny(register).run(input.toMutableList())
        return register["a"]!!
    }
}