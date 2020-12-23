package y2016

import Day
import utils.IO


class Day25 : Day() {

    override val input = IO.readStrings(2016, 25)

    override fun solve1(): Int {
        val regex = Regex("^(!?01)+")
        var output = ""
        var value = -1
        while (!regex.matches(output)) {
            output = ""
            value++
            Assembunny(mutableMapOf("a" to value, "b" to 0, "c" to 0, "d" to 0)).run(input.toMutableList()) {
                output += it
                output.length < 10
            }
        }
        return value
    }

    override fun solve2() {

    }
}