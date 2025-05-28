package y2024

import Day
import kotlin.math.abs

class Day01 : Day<Pair<List<Long>, List<Long>>>(2024, 1) {

    override suspend fun List<String>.parse(): Pair<List<Long>, List<Long>> {
        val a = mutableListOf<Long>()
        val b = mutableListOf<Long>()
        forEach { line ->
            line.split(Regex("\\s+")).let {
                a += it.first().toLong()
                b += it.last().toLong()
            }
        }
        return a to b
    }

    override suspend fun solve1(input: Pair<List<Long>, List<Long>>) =
        input.first.sorted().zip(input.second.sorted()).sumOf { abs(it.first - it.second) }

    override suspend fun solve2(input: Pair<List<Long>, List<Long>>) =
        input.first.sumOf { it * input.second.count { b -> b == it } }
}