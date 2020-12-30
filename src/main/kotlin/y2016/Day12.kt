package y2016

import Day

class Day12 : Day(2016, 12) {

    override val input = readStrings().toMutableList()

    private val register = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)

    override fun solve1(): Int {
        val register = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)
        Assembunny(register).run(input)
        return register["a"]!!
    }

    override fun solve2(): Int {
        val register = mutableMapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0)
        Assembunny(register).run(input)
        return register["a"]!!
    }
}