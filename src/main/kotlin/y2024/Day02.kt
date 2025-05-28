package y2024

import Day
import kotlin.math.abs

class Day02 : Day<List<List<Int>>>(2024, 2) {

    override suspend fun List<String>.parse() = map { it.split(" ").map(String::toInt) }

    override suspend fun solve1(input: List<List<Int>>) = input.count { it.validate() }

    override suspend fun solve2(input: List<List<Int>>) = input.count { report ->
        report.validate() || report.indices.any { indexToRemove ->
            report.filterIndexed { index, i -> index != indexToRemove }.validate()
        }
    }

    private fun List<Int>.validate() = when {
        this[0] == this[1] -> false
        this[0] < this[1] -> (0 until lastIndex).all { this[it] < this[it + 1] && abs(this[it] - this[it + 1]) <= 3 }
        else -> (0 until lastIndex).all { this[it] > this[it + 1] && abs(this[it] - this[it + 1]) <= 3 }
    }
}