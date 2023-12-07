package y2021

import Day
import utils.stepSum
import kotlin.math.max
import kotlin.math.min

class Day07 : Day<List<String>>(2021, 7) {

    override fun List<String>.parse() = first().split(",").map { it.toInt() }

    override fun solve1(input: List<String>): Int {
        var minFuel = Int.MAX_VALUE
        (input.minOrNull()!!..input.maxOrNull()!!).forEach {  pos ->
            minFuel = min(minFuel, input.sumOf { max(it, pos) - min(it, pos) })
        }
        return minFuel
    }

    override fun solve2(input: List<String>): Int {
        var minFuel = Int.MAX_VALUE
        (input.minOrNull()!!..input.maxOrNull()!!).forEach {  pos ->
            minFuel = min(minFuel, input.sumOf { (max(it, pos) - min(it, pos)).stepSum() })
        }
        return minFuel
    }
}