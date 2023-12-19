package y2017

import Day

class Day05 : Day<List<Int>>(2017, 5) {

    override suspend fun List<String>.parse() = this.map { it.toInt() }

    override suspend fun solve1(input: List<Int>): Int {
        val instructions = input.toIntArray()
        var index = 0
        var count = 0
        while (index in instructions.indices) {
            val jump = instructions[index]
            instructions[index]++
            index += jump
            count++
        }
        return count
    }

    override suspend fun solve2(input: List<Int>): Int {
        val instructions = input.toIntArray()
        var index = 0
        var count = 0
        while (index in instructions.indices) {
            val jump = instructions[index]
            if (jump >= 3) instructions[index]-- else instructions[index]++
            index += jump
            count++
        }
        return count
    }
}