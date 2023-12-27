package y2018

import Day

class Day12 : Day<Pair<String, Set<String>>>(2018, 12) {

    override suspend fun List<String>.parse() = first().substring(15) to drop(2)
        .filter { it.endsWith("#") }
        .map { it.take(5) }
        .toSet()

    override suspend fun solve1(input: Pair<String, Set<String>>) = mutatePlants(input.first, input.second).drop(19).first().second

    override suspend fun solve2(input: Pair<String, Set<String>>): Long {
        var previousDiff = 0L
        var previousSize = 0L
        var generationNumber = 0

        mutatePlants(input.first, input.second).dropWhile { thisGen ->
            val thisDiff = thisGen.second - previousSize
            if (thisDiff != previousDiff) {
                previousDiff = thisDiff
                previousSize = thisGen.second
                generationNumber += 1
                true
            } else {
                false
            }
        }.first()

        return previousSize + (previousDiff * (50_000_000_000 - generationNumber))
    }

    private fun mutatePlants(state: String, rules: Set<String>): Sequence<Pair<String, Long>> = sequence {
        var zeroIndex = 0
        var currentState = state
        while (true) {
            while (!currentState.startsWith(".....")) {
                currentState = ".$currentState"
                zeroIndex++
            }
            while (!currentState.endsWith(".....")) {
                currentState = "$currentState."
            }

            currentState = currentState
                .toList()
                .windowed(5, 1)
                .map { it.joinToString(separator = "") }
                .map { if (it in rules) '#' else '.' }
                .joinToString(separator = "")

            zeroIndex -= 2
            yield(Pair(currentState, currentState.sumIndexesFrom(zeroIndex)))
        }
    }

    private fun String.sumIndexesFrom(zero: Int) = mapIndexed { idx, c -> if (c == '#') idx.toLong() - zero else 0 }.sum()
}