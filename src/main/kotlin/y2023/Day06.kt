package y2023

import Day

class Day06 : Day<List<String>>(2023, 6) {

    override val input = readStrings().map { line ->
        line.split(":")[1].trim().split(Regex("\\s+")).map { it.toLong() }
    }

    private val times = input[0]
    private val highscores = input[1]

    override fun solve1(input: List<String>) = times.indices.map { times[it].countWins(highscores[it]) }.reduce { acc, i -> acc * i }

    override fun solve2(input: List<String>): Int {
        val singleTime = times.joinToString("").toLong()
        val singleHighscore = highscores.joinToString("").toLong()
        return singleTime.countWins(singleHighscore)
    }

    private fun Long.countWins(highscore: Long) = (1 until this).count { getDistance(it) > highscore }

    private fun Long.getDistance(charge: Long) = (this - charge) * charge
}