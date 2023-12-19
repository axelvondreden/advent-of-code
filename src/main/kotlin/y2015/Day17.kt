package y2015

import Day
import utils.Point
import utils.sumByLong

class Day17 : Day<List<Point>>(2015, 17) {

    override suspend fun List<String>.parse() = mapIndexed { index, nr -> Point(index.toLong(), nr.toLong()) }

    private val combinations = mutableListOf<List<Point>>()

    override suspend fun solve1(input: List<Point>): Int {
        fill(input, mutableListOf(), 150)
        return combinations.size
    }

    override suspend fun solve2(input: List<Point>): Int {
        val min = combinations.minOf { it.size }
        return combinations.count { it.size == min }
    }

    private fun fill(input: List<Point>, chain: MutableList<Point>, expected: Long) {
        val sum = chain.sumByLong { it.y }
        when {
            sum == expected && !combinations.containsList(chain) -> combinations.add(chain)
            sum > expected -> return
            else -> {
                val options = input.filter {
                    it.x > (chain.lastOrNull()?.x ?: -1) && it !in chain && sum + it.y <= expected
                }
                options.forEach { fill(input, chain.toTypedArray().copyOf().toMutableList().apply { add(it) }, expected) }
            }
        }
    }

    private fun MutableList<List<Point>>.containsList(search: List<Point>) = any { it.containsAll(search) }
}