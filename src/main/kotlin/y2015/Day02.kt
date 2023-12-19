package y2015

import Day

class Day02 : Day<List<List<Int>>>(2015, 2) {

    override suspend fun List<String>.parse() = map { it.split("x").map(String::toInt).sorted() }

    override suspend fun solve1(input: List<List<Int>>) =
        input.sumOf { (3 * it[0] * it[1]) + (2 * it[1] * it[2]) + (2 * it[2] * it[0]) }

    override suspend fun solve2(input: List<List<Int>>) =
        input.sumOf { (2 * it[0]) + (2 * it[1]) + (it[0] * it[1] * it[2]) }
}