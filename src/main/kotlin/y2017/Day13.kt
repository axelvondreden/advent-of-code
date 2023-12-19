package y2017

import Day

class Day13 : Day<List<String>>(2017, 13) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>) = input.toLayers().filter { it.caughtAtTime(0) }.sumOf { it.severity }

    override suspend fun solve2(input: List<String>): Int {
        val layers = input.toLayers()
        return generateSequence(0, Int::inc).filter { time -> layers.none { it.caughtAtTime(time) } }.first()
    }

    private fun List<String>.toLayers() = map {
        val split = it.split(": ")
        Layer(split[0].toInt(), split[1].toInt())
    }

    private data class Layer(private val depth: Int, private val range: Int) {
        val severity = depth * range
        fun caughtAtTime(time: Int) = (time + depth) % ((range - 1) * 2) == 0
    }
}
