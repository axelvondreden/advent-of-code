package y2017

import Day
import utils.IO

class Day06 : Day() {

    override val input = IO.readString(2017, 6).split(Regex("\\s+")).map { it.toInt() }

    override fun solve1(): Int {
        val states = mutableSetOf<String>()
        var steps = 0
        var banks = input.toIntArray()
        while (!states.contains(banks.joinToString("|"))) {
            states.add(banks.joinToString("|"))
            banks = banks.redistribute()
            steps++
        }
        return steps
    }

    override fun solve2(): Int {
        val states = mutableListOf<String>()
        var banks = input.toIntArray()
        while (!states.contains(banks.joinToString("|"))) {
            states.add(banks.joinToString("|"))
            banks = banks.redistribute()
        }
        return states.size - states.indexOf(banks.joinToString("|"))
    }

    private fun IntArray.redistribute(): IntArray {
        val bank = clone()
        var amount = bank.maxOrNull()!!
        var index = bank.indexOf(amount)
        bank[index] = 0
        if (index < size - 1) index++ else index = 0
        while (amount > 0) {
            bank[index]++
            amount--
            if (index < size - 1) index++ else index = 0
        }
        return bank
    }
}
