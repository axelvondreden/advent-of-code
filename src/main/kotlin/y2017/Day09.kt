package y2017

import Day
import utils.IO

class Day09 : Day() {

    override val input = IO.readString(2017, 9)

    override fun solve1() = input.filterGarbage().calcGroupScore()

    override fun solve2() = input.countGarbage()

    private fun String.filterGarbage(): String {
        var new = ""
        var canceled = false
        var inGarbage = false
        forEach {
            if (canceled) canceled = false
            else if (inGarbage) {
                if (it == '!') canceled = true
                else if (it == '>') inGarbage = false
            }
            else if (it == '<') inGarbage = true
            else new += it
        }
        return new
    }

    private fun String.calcGroupScore(): Int {
        var lvl = 0
        var score = 0
        forEach {
            if (it == '{') {
                lvl++
            } else if (it == '}') {
                score += lvl
                lvl--
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
