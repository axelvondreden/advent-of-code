package y2015

import Day
import utils.IO

class Day02 : Day() {

    override val input = IO.readStrings(2015, 2).map { it.split("x").map(String::toInt).sorted() }

    override fun solve1() = input.sumBy { (3 * it[0] * it[1]) + (2 * it[1] * it[2]) + (2 * it[2] * it[0]) }

    override fun solve2() = input.sumBy { (2 * it[0]) + (2 * it[1]) + (it[0] * it[1] * it[2]) }
}