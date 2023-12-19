package y2018

import Day

class Day15 : Day<String>(2018, 15) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String): String {
        return ""
    }

    override suspend fun solve2(input: String): Int {
        return 0
    }
}