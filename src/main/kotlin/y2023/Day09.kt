package y2023

import Day

class Day09 : Day<List<List<Long>>>(2023, 9) {

    override suspend fun List<String>.parse() = map { line -> line.split(" ").map { it.toLong() } }

    override suspend fun solve1(input: List<List<Long>>) = input.sumOf { it.extrapolateNextValue() }

    override suspend fun solve2(input: List<List<Long>>) = input.sumOf { it.extrapolatePreviousValue() }

    private fun List<Long>.extrapolateNextValue(): Long {
        val histories =getHistories()
        var lastDiff = 0L
        histories.reversed().drop(1).forEach { h ->
            val nextNr = h.last() + lastDiff
            lastDiff = nextNr
        }
        return lastDiff
    }

    private fun List<Long>.extrapolatePreviousValue(): Long {
        val histories = getHistories()
        var lastDiff = 0L
        histories.reversed().drop(1).forEach { h ->
            val nextNr = h.first() - lastDiff
            lastDiff = nextNr
        }
        return lastDiff
    }

    private fun List<Long>.getHistories(): List<List<Long>> {
        val histories = mutableListOf(this)
        while (!histories.last().all { it == 0L }) {
            histories += histories.last().getDeltas()
        }
        return histories
    }

    private fun List<Long>.getDeltas() = zipWithNext { a, b -> b - a }
}