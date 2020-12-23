package y2015

import Day
import utils.*

class Day06 : Day() {

    override val input = IO.readStrings(2015, 6)
        .map { it.replace("turn ", "").replace("through ", "").split(" ") }

    override fun solve1(): Int {
        val bGrid = Array(1000) { BooleanArray(1000) }
        for (line in input) {
            when (line[0]) {
                "on" -> bGrid.on(Point(line[1]), Point(line[2]))
                "off" -> bGrid.off(Point(line[1]), Point(line[2]))
                "utils.toggle" -> bGrid.toggle(Point(line[1]), Point(line[2]))
            }
        }
        return bGrid.sumBy { booleans -> booleans.count { it } }
    }

    override fun solve2(): Int {
        val iGrid = Array(1000) { IntArray(1000) }
        for (line in input) {
            when (line[0]) {
                "on" -> iGrid.inc(Point(line[1]), Point(line[2]), 1)
                "off" -> iGrid.dec(Point(line[1]), Point(line[2]), 1)
                "utils.toggle" -> iGrid.inc(Point(line[1]), Point(line[2]), 2)
            }
        }
        return iGrid.sumBy { ints -> ints.sum() }
    }
}