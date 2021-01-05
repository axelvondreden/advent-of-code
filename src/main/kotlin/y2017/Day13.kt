package y2017

import Day

class Day13 : Day(2017, 13) {

    override val input = readStrings()

    override fun solve1() = input.toLayers().filter { it.caughtAtTime(0) }.sumBy { it.severity }

    override fun solve2(): Int {
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
