package y2016

import Day


class Day15 : Day<Any?>(2016,  15) {

    override val input = readStrings()

    override fun solve1(input: List<String>) = simulate(parseDisks(input))

    override fun solve2(input: List<String>) = simulate(parseDisks(input).plus(Disc(11, 0)))

    private fun parseDisks(input: List<String>) = input.map {
        val split = it.split(" ")
        Disc(split[3].toInt(), split[11].dropLast(1).toInt())
    }

    private fun simulate(discs: List<Disc>): Int {
        var i = 0
        main@
        while (true) {
            for (j in discs.indices) {
                val d = discs[j]
                if ((d.current + (i + j + 1)) % d.positions != 0) {
                    i++
                    continue@main
                }
            }
            return i
        }
    }

    private data class Disc(val positions: Int, val current: Int)
}