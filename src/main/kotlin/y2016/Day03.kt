package y2016

import Day

class Day03 : Day(2016, 3) {

    override val input = readStrings().map { it.split(" ").map(String::toInt) }

    override fun solve1() = input.map { it.sorted() }.count { it[0] + it[1] > it[2] }

    override fun solve2(): Int {
        var count = 0
        (input.indices step 3).forEach { i ->
            repeat((0..2).map {
                intArrayOf(input[i][it], input[i + 1][it], input[i + 2][it]).sorted()
            }.filter { it[0] + it[1] > it[2] }.size) { count++ }
        }
        return count
    }
}