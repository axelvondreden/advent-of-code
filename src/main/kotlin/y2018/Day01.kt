package y2018

import Day

class Day01 : Day<List<Int>>(2018, 1) {

    override suspend fun List<String>.parse() = map { it.toInt() }

    override suspend fun solve1(input: List<Int>) = input.fold(0) { sum, frq -> sum + frq }

    override suspend fun solve2(input: List<Int>): Int {
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