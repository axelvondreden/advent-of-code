package y2020

import Day


class Day15 : Day<Map<Int, Int>>(2020, 15) {

    override suspend fun List<String>.parse() = first().split(",").mapIndexed { index, s -> s.toInt() to index }.toMap()

    override suspend fun solve1(input: Map<Int, Int>) = getNthSpokenNumber(input.toMutableMap(), 2020)

    override suspend fun solve2(input: Map<Int, Int>) = getNthSpokenNumber(input.toMutableMap(), 30000000)

    private fun getNthSpokenNumber(spoken: MutableMap<Int, Int>, n: Int): Int {
        var turn = spoken.size
        var last = spoken.maxByOrNull { it.value }!!.key
        var delayed = -1
        while (turn < n) {
            last = if (spoken.getOrDefault(last, turn) < turn - 1) turn - spoken[last]!! - 1 else 0
            if (delayed >= 0) spoken[delayed] = turn - 1
            delayed = last
            turn++
        }
        return last
    }
}