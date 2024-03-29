package y2021

import Day

class Day04 : Day<List<String>>(2021, 4) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val numbers = input[0].split(",").map { it.toInt() }
        val maps = input.filter { it.isNotBlank() }.drop(1).chunked(5).map { list ->
            BingoField((0..4).map { row -> list[row].trim().split(Regex("\\W+")).map { it.toInt() } })
        }
        numbers.forEach { nr ->
            maps.forEach { map ->
                map.mark(nr)
            }
            maps.firstOrNull { it.checkForWin() }?.let { return it.calcScore(nr) }
        }
        return 0
    }

    override suspend fun solve2(input: List<String>): Int {
        val numbers = input[0].split(",").map { it.toInt() }
        val maps = input.filter { it.isNotBlank() }.drop(1).chunked(5).map { list ->
            BingoField((0..4).map { row -> list[row].trim().split(Regex("\\W+")).map { it.toInt() } })
        }
        maps.forEach { it.reset() }
        var unfinishedMaps = maps
        numbers.forEach { nr ->
            unfinishedMaps.forEach { map ->
                map.mark(nr)
            }
            if (unfinishedMaps.size == 1 && unfinishedMaps[0].checkForWin()) {
                return unfinishedMaps[0].calcScore(nr)
            }
            unfinishedMaps = unfinishedMaps.filter { !it.checkForWin() }
        }
        return 0
    }

    private class BingoField(inputNumbers: List<List<Int>>) {
        private val field = inputNumbers.map { row -> row.map { BingoNumber(it) } }

        fun checkForWin() = field.any { r -> r.all { it.marked } } || (0..4).any { nr -> field.all { it[nr].marked } }

        fun mark(nr: Int) {
            field.forEach { row -> row.forEach { if (it.nr == nr) it.marked = true } }
        }

        fun calcScore(lastNr: Int) = lastNr * field.sumOf { row -> row.filter { !it.marked }.sumOf { it.nr } }

        fun reset() {
            field.forEach { row -> row.forEach { it.marked = false } }
        }
    }

    private data class BingoNumber(val nr: Int, var marked: Boolean = false)
}