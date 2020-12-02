package y2020

import Day
import Utils

class Day02 : Day() {

    override val input = Utils.readStrings(2020, 2).map {
        val split = it.split(":")
        val range = split[0].split(" ")[0].split("-")
        Password(range[0].toInt()..range[1].toInt(), split[0].split(" ")[1][0], split[1].trim())
    }

    override fun solve1() = input.count { it.isValid() }

    override fun solve2(): Int {
        return 0
    }

    data class Password(val policyRange: IntRange, val policyChar: Char, val password: String)

    private fun Password.isValid() = password.count { it == policyChar } in policyRange
}