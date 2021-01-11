package y2018

import Day

class Day01 : Day(2018, 1) {

    override val input = readStrings().map { it.toInt() }

    override fun solve1() = input.fold(0) { sum, frq -> sum + frq }

    override fun solve2(): Int {
        val frequencies = mutableSetOf(0)
        var current = 0
        var index = 0
        while (true) {
            current += input[index]
            if (!frequencies.add(current)) return current
            if (index == input.size - 1) index = 0
            else index++
        }
    }
}