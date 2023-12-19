package y2021

import Day
import utils.stepSum
import kotlin.math.max
import kotlin.math.min

class Day07 : Day<List<Int>>(2021, 7) {

    override suspend fun List<String>.parse() = first().split(",").map { it.toInt() }

    override suspend fun solve1(input: List<Int>): Int {
        var minFuel = Int.MAX_VALUE
        (input.minOrNull()!!..input.maxOrNull()!!).forEach {  pos ->
            minFuel = min(minFuel, input.sumOf { max(it, pos) - min(it, pos) })
        }
        return minFuel
    }

    override suspend fun solve2(input: List<Int>): Int {
        var minFuel = Int.MAX_VALUE
        (input.minOrNull()!!..input.maxOrNull()!!).forEach {  pos ->
            minFuel = min(minFuel, input.sumOf { (max(it, pos) - min(it, pos)).stepSum() })
        }
        return minFuel
    }
}