package y2017

import Day

class Day23 : Day<Any?>(2017, 23) {

    override val input = readStrings()

    override fun solve1(input: List<String>): Int {
        val register = ('a'..'h').associateWith { 0L }.toMutableMap()
        var index = 0
        var mulCounter = 0
        while (index in input.indices) {
            if (input[index].startsWith("mul ")) {
                mulCounter++
            }
            index += register.step(input[index])
        }
        return mulCounter
    }

    override fun solve2(input: List<String>): Int {
        val b = input[0].split(" ")[2].toInt() * 100 + 100000
        return (b..b + 17000 step 17).count { !it.toBigInteger().isProbablePrime(2) }
    }

    private fun MutableMap<Char, Long>.step(cmd: String): Int {
        val split = cmd.split(" ")
        when (split[0]) {
            "set" -> put(split[1][0], getValue(split[2]))
            "sub" -> put(split[1][0], get(split[1][0])!! - getValue(split[2]))
            "mul" -> put(split[1][0], get(split[1][0])!! * getValue(split[2]))
            "jnz" -> if (getValue(split[1]) != 0L) return getValue(split[2]).toInt()
        }
        return 1
    }

    private fun MutableMap<Char, Long>.getValue(at: String) = at.toLongOrNull() ?: get(at[0])!!
}
