package y2023

import Day
import kotlin.math.pow

class Day04 : Day<List<String>>(2023, 4) {

    override val input = readStrings()

    private val games = input.map { line ->
        val split = line.removePrefix("Card ").split(":")
        val split2 = split[1].trim().split("|")
        Game(
            nr = split[0].trim().toInt(),
            winners = split2[0].trim().split(Regex("\\s+")).map { it.toInt() },
            numbers = split2[1].trim().split(Regex("\\s+")).map { it.toInt() }
        )
    }

    override fun solve1(input: List<String>) = games.sumOf { it.score }

    override fun solve2(input: List<String>): Int {
        val array = IntArray(games.size) { 1 }
        games.forEachIndexed { index, game ->
            for (i in index + 1..index + game.winnerCount) {
                array[i] += array[index]
            }
        }
        return array.sum()
    }

    private data class Game(val nr: Int, val winners: List<Int>, val numbers: List<Int>) {
        val winnerCount get() = numbers.count { it in winners }
        val score get() = 2.0.pow(winnerCount.toDouble() - 1).toInt()
    }
}