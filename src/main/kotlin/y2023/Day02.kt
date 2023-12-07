package y2023

import Day

class Day02 : Day<List<String>>(2023, 2) {

    override val input = readStrings()

    private val games = input.map { game ->
        val s1 = game.split(": ")
        val id = s1[0].removePrefix("Game ").toInt()
        val s2 = s1[1].split("; ")
        val rounds = s2.map { round ->
            val vals = round.split(", ")
            val r = vals.firstOrNull { it.endsWith("red") }?.split(" ")?.get(0)?.toInt() ?: 0
            val g = vals.firstOrNull { it.endsWith("green") }?.split(" ")?.get(0)?.toInt() ?: 0
            val b = vals.firstOrNull { it.endsWith("blue") }?.split(" ")?.get(0)?.toInt() ?: 0
            Gameround(r, g, b)
        }
        Game(id, rounds)
    }

    override fun solve1(input: List<String>) = games.filter { it.isPossible(12, 13, 14) }.sumOf { it.id }

    override fun solve2(input: List<String>) = games.sumOf { it.getLowestProduct() }
    
    private data class Game(val id: Int, val rounds: List<Gameround>) {

        fun isPossible(r: Int, g: Int, b: Int) = rounds.none { it.r > r || it.g > g || it.b > b }

        fun getLowestProduct() = rounds.maxOf { it.r } * rounds.maxOf { it.g } * rounds.maxOf { it.b }
    }
    
    private data class Gameround(val r: Int, val g: Int, val b: Int)
}