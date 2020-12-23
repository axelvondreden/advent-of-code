package y2017

import Day
import utils.IO

class Day05 : Day() {

    override val input = IO.readStrings(2017, 5).map { it.toInt() }

    override fun solve1(): Int {
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

    override fun solve2(): Int {
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