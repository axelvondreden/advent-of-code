package y2016

import Day


class Day15 : Day<List<Day15.Disc>>(2016,  15) {

    override suspend fun List<String>.parse() = map {
        val split = it.split(" ")
        Disc(split[3].toInt(), split[11].dropLast(1).toInt())
    }

    override suspend fun solve1(input: List<Disc>) = simulate(input)

    override suspend fun solve2(input: List<Disc>) = simulate(input.plus(Disc(11, 0)))

    private fun simulate(discs: List<Disc>) = generateSequence(0) { it + 1 }.first { possibleTime ->
        discs.indices.all { j ->
            val d = discs[j]
            (d.current + (possibleTime + j + 1)) % d.positions == 0
        }
    }

    data class Disc(val positions: Int, val current: Int)
}