package y2015

import Day

class Day02 : Day(2015, 2) {

    override val input = readStrings().map { it.split("x").map(String::toInt).sorted() }

    override fun solve1() = input.sumOf { (3 * it[0] * it[1]) + (2 * it[1] * it[2]) + (2 * it[2] * it[0]) }

    override fun solve2() = input.sumOf { (2 * it[0]) + (2 * it[1]) + (it[0] * it[1] * it[2]) }
}