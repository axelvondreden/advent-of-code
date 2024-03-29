package y2015

import Day
import utils.permute

class Day13 : Day<Map<Pair<String, String>, Int>>(2015, 13) {

    override suspend fun List<String>.parse() = asSequence().parse()

    override suspend fun solve1(input: Map<Pair<String, String>, Int>) = input.maxHappiness()

    override suspend fun solve2(input: Map<Pair<String, String>, Int>) = input.maxHappinessWithGap()

    private fun Sequence<String>.parse(): Map<Pair<String, String>, Int> = fold(hashMapOf()) { map, line ->
        val tokens = line.split(" ")
        val happiness = if (tokens[2] == "lose") -(tokens[3].toInt()) else tokens[3].toInt()
        map.apply {
            val pair = Pair(tokens.first(), tokens.last().dropLast(1))
            put(pair, happiness)
        }
    }

    private fun Map<Pair<String, String>, Int>.maxHappiness() =
        keys.map { it.first }.distinct().toSet().permute().map { it + it.first() }.maxOf { seatList ->
            seatList.zipWithNext().sumOf { get(it)!! } +
                    seatList.reversed().zipWithNext().sumOf { get(it)!! }
        }

    private fun Map<Pair<String, String>, Int>.maxHappinessWithGap() =
        keys.map { it.first }.distinct().toSet().permute().map { it + it.first() }.maxOf { seatList ->
            (0 until seatList.size - 1).maxOf { gap ->
                seatList.zipWithNext().dropAt(gap).sumOf { get(it)!! } +
                        seatList.reversed().zipWithNext().dropAt(seatList.size - 2 - gap).sumOf { get(it)!! }
            }
        }

    private fun <T> List<T>.dropAt(n: Int): List<T> = subList(0, n) + subList(n + 1, size)
}