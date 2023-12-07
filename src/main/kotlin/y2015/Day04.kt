package y2015

import Day
import utils.md5

class Day04 : Day<String>(2015, 4) {

    override fun List<String>.parse() = this[0]

    override fun solve1(input: String) = input.findLowest("00000")

    override fun solve2(input: String) = input.findLowest("000000")

    private fun String.findLowest(target: String): Int {
        var i = 1
        while (true) {
            if ("$this$i".md5().startsWith(target)) {
                return i
            } else {
                i++
            }
        }
    }
}