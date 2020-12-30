package y2015

import Day
import utils.md5

class Day04 : Day(2015, 4) {

    override val input = readString()

    override fun solve1() = findLowest("00000")

    override fun solve2() = findLowest("000000")

    private fun findLowest(target: String): Int {
        var i = 1
        while (true) {
            if ("$input$i".md5().startsWith(target)) {
                return i
            } else {
                i++
            }
        }
    }
}