package y2017

import Day

class Day09 : Day<String>(2017, 9) {

    override fun List<String>.parse() = first()

    override fun solve1(input: String) = input.filterGarbage().calcGroupScore()

    override fun solve2(input: String) = input.countGarbage()

    private fun String.filterGarbage(): String {
        var new = ""
        var canceled = false
        var inGarbage = false
        forEach {
            when {
                canceled -> canceled = false
                inGarbage -> {
                    when (it) {
                        '!' -> canceled = true
                        '>' -> inGarbage = false
                    }
                }
                it == '<' -> inGarbage = true
                else -> new += it
            }
        }
        return new
    }

    private fun String.calcGroupScore(): Int {
        var lvl = 0
        var score = 0
        forEach {
            when (it) {
                '{' -> lvl++
                '}' -> {
                    score += lvl
                    lvl--
                }
            }
        }
        return score
    }

    private fun String.countGarbage(): Int {
        var count = 0
        var canceled = false
        var inGarbage = false
        forEach {
            when {
                canceled -> canceled = false
                inGarbage -> when (it) {
                    '!' -> canceled = true
                    '>' -> inGarbage = false
                    else -> count++
                }
                it == '<' -> inGarbage = true
            }
        }
        return count
    }
}
