package y2015

import Day

class Day23 : Day<List<String>>(2015, 23) {

    override suspend fun List<String>.parse() = map { it.replace(",", "") }

    var index = 0

    override suspend fun solve1(input: List<String>): Long {
        index = 0
        val register = mutableMapOf("a" to 0L, "b" to 0L)
        while (index in input.indices) {
            register.run(input)
        }
        return register["b"]!!
    }

    override suspend fun solve2(input: List<String>): Long {
        index = 0
        val register = mutableMapOf("a" to 1L, "b" to 0L)
        while (index in input.indices) {
            register.run(input)
        }
        return register["b"]!!
    }

    private fun MutableMap<String, Long>.run(input: List<String>) {
        val inst = input[index].split(" ")
        when (inst[0]) {
            "hlf" -> {
                put(inst[1], get(inst[1])!! / 2)
                index++
            }
            "tpl" -> {
                put(inst[1], get(inst[1])!! * 3)
                index++
            }
            "inc" -> {
                put(inst[1], get(inst[1])!! + 1)
                index++
            }
            "jmp" -> index += inst[1].toInt()
            "jie" -> if (get(inst[1])!! % 2 == 0L) index += inst[2].toInt() else index++
            "jio" -> if (get(inst[1])!! == 1L) index += inst[2].toInt() else index++
        }
    }
}