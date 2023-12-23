package y2023

import Day
import utils.toCharMatrix

class Day23 : Day<Array<CharArray>>(2023, 23) {

    override suspend fun List<String>.parse() = toCharMatrix()

    override suspend fun solve1(input: Array<CharArray>): Any = 0

    override suspend fun solve2(input: Array<CharArray>): Any = 0
}