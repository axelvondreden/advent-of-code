package y2015

import Day
import utils.permute

class Day13 : Day(2015, 13) {

    override val input = readStrings().asSequence().parse()

    override fun solve1() = input.maxHappiness()

    override fun solve2() = input.maxHappinessWithGap()

    private fun Sequence<String>.parse(): Map<Pair<String, String>, Int> = fold(hashMapOf()) { map, line ->
        val tokens = line.split(" ")
        val happiness = if (tokens[2] == "lose") -(tokens[3].toInt()) else tokens[3].toInt()
        map.apply {
            val pair = Pair(tokens.first(), tokens.last().dropLast(1))
            put(pair, happiness)
        }
    }

    private fun Map<Pair<String, String>, Int>.maxHappiness() =
        keys.map { it.first }.distinct().toSet().permute().map {
            it + it.first()
        }.map { seatList ->
            seatList.zipWithNext().map { get(it)!! }.sum() +
                    seatList.reversed().zipWithNext().map { get(it)!! }.sum()
        }.maxOrNull()!!

    private fun Map<Pair<String, String>, Int>.maxHappinessWithGap() =
        keys.map { it.first }.distinct().toSet().permute().map {
            it + it.first()
        }.map { seatList ->
            (0 until seatList.size - 1).map { gap ->
                seatList.zipWithNext().dropAt(gap).map { get(it)!! }.sum() +
                        seatList.reversed().zipWithNext().dropAt(seatList.size - 2 - gap).map { get(it)!! }.sum()
            }.maxOrNull()!!
        }.maxOrNull()!!

    private fun <T> List<T>.dropAt(n: Int): List<T> = subList(0, n) + subList(n + 1, size)
}