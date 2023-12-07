package y2016

import Day

class Day03 : Day<List<List<Int>>>(2016, 3) {

    override fun List<String>.parse() = map { it.split(" ").map(String::toInt) }

    override fun solve1(input: List<List<Int>>) = input.map { it.sorted() }.count { it[0] + it[1] > it[2] }

    override fun solve2(input: List<List<Int>>): Int {
        var count = 0
        (input.indices step 3).forEach { i ->
            repeat((0..2).map {
                intArrayOf(input[i][it], input[i + 1][it], input[i + 2][it]).sorted()
            }.filter { it[0] + it[1] > it[2] }.size) { count++ }
        }
        return count
    }
}