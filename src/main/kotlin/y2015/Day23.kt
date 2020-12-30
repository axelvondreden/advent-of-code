package y2015

import Day

class Day23 : Day(2015, 23) {

    override val input = readStrings().map { it.replace(",", "") }

    var index = 0

    override fun solve1(): Long {
        index = 0
        val register = mutableMapOf("a" to 0L, "b" to 0L)
        while (index in input.indices) {
            run(register)
        }
        return register["b"]!!
    }

    override fun solve2(): Long {
        index = 0
        val register = mutableMapOf("a" to 1L, "b" to 0L)
        while (index in input.indices) {
            run(register)
        }
        return register["b"]!!
    }

    fun run(register: MutableMap<String, Long>) {
        val inst = input[index].split(" ")
        when (inst[0]) {
            "hlf" -> {
                register[inst[1]] = register[inst[1]]!! / 2
                index++
            }
            "tpl" -> {
                register[inst[1]] = register[inst[1]]!! * 3
                index++
            }
            "utils.inc" -> {
                register[inst[1]] = register[inst[1]]!! + 1
                index++
            }
            "jmp" -> index += inst[1].toInt()
            "jie" -> {
                if (register[inst[1]]!! % 2 == 0L) {
                    index += inst[2].toInt()
                } else {
                    index++
                }
            }
            "jio" -> {
                if (register[inst[1]]!! == 1L) {
                    index += inst[2].toInt()
                } else {
                    index++
                }
            }
        }
    }
}