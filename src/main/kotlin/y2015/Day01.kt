package y2015

import Day
import runner.Viz

class Day01 : Day<String>(2015, 1) {

    override fun List<String>.parse() = this[0]

    override fun solve1(input: String) = input.count { it == '(' } - input.count { it == ')' }

    override fun solve2(input: String): Int {
        var floor = 0
        for ((index, c) in input.withIndex()) {
            when (c) {
                '(' -> floor++
                ')' -> floor--
            }
            if (floor < 0) return index + 1
        }
        return 0
    }

    override suspend fun solve1Visualized(input: String, onProgress: suspend (Viz) -> Unit): Int {
        var lvl = 0
        input.forEachIndexed { index, c ->
            if (c == '(') {
                lvl++
            } else {
                lvl--
            }
            onProgress(Viz((index + 1).toDouble() / input.length))
        }
        return lvl
    }
}