package y2017

import Day

class Day06 : Day<List<Int>>(2017, 6) {

    override suspend fun List<String>.parse() = first().split(Regex("\\s+")).map { it.toInt() }

    override suspend fun solve1(input: List<Int>): Int {
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

    override suspend fun solve2(input: List<Int>): Int {
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
