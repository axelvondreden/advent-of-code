package y2016

import Day

class Day12 : Day() {

    override val input = Utils.readStrings(2016, 12)

    private val registers = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)
    private var index = 0

    override fun solve1(): Int {
        run()
        return registers["a"]!!
    }

    override fun solve2() = "MISSING"

    private fun run() {
        index = 0
        while (index < input.size) {
            val split = input[index].split(" ")
            val cmd = split[0]
            val arg1 = split[1]
            val arg2 = split.getOrNull(2)
            when (cmd) {
                "cpy" -> {
                    registers[arg2!!] = arg1.toIntOrNull() ?: registers[arg1]!!
                    index++
                }
                "inc" -> {
                    registers[arg1] = registers[arg1]!! + 1
                    index++
                }
                "dec" -> {
                    registers[arg1] = registers[arg1]!! - 1
                    index++
                }
                "jnz" -> if (arg1.toIntOrNull() ?: registers[arg1]!! != 0) index += arg2!!.toInt() else index++
            }
        }
    }
}