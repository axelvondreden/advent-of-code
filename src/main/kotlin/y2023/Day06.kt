package y2023

import Day

class Day06 : Day<List<List<Long>>>(2023, 6) {

    private lateinit var times: List<Long>
    private lateinit var highscores: List<Long>

    override fun List<String>.parse() = map { line ->
        line.split(":")[1].trim().split(Regex("\\s+")).map { it.toLong() }
    }

    override fun solve1(input: List<List<Long>>) = times.indices.map { times[it].countWins(highscores[it]) }.reduce { acc, i -> acc * i }

    override fun solve2(input: List<List<Long>>): Int {
        val singleTime = times.joinToString("").toLong()
        val singleHighscore = highscores.joinToString("").toLong()
        return singleTime.countWins(singleHighscore)
    }

    private fun Long.countWins(highscore: Long) = (1 until this).count { getDistance(it) > highscore }

    private fun Long.getDistance(charge: Long) = (this - charge) * charge
}