package y2023

import Day
import kotlin.math.pow

class Day04 : Day<List<Day04.Game>>(2023, 4) {

    override suspend fun List<String>.parse() = map { line ->
        val split = line.removePrefix("Card ").split(":")
        val split2 = split[1].trim().split("|")
        Game(
            nr = split[0].trim().toInt(),
            winners = split2[0].trim().split(Regex("\\s+")).map { it.toInt() },
            numbers = split2[1].trim().split(Regex("\\s+")).map { it.toInt() }
        )
    }

    override suspend fun solve1(input: List<Game>) = input.sumOf { it.score }

    override suspend fun solve2(input: List<Game>): Int {
        val array = IntArray(input.size) { 1 }
        input.forEachIndexed { index, game ->
            for (i in index + 1..index + game.winnerCount) {
                array[i] += array[index]
            }
        }
        return array.sum()
    }

    data class Game(val nr: Int, val winners: List<Int>, val numbers: List<Int>) {
        val winnerCount get() = numbers.count { it in winners }
        val score get() = 2.0.pow(winnerCount.toDouble() - 1).toInt()
    }
}