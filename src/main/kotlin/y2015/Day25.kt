package y2015

import Day

class Day25 : Day<Pair<Int, Int>>(2015, 25) {

    override suspend fun List<String>.parse() = with(this[0].split("at row ")[1].split(" ")) {
        get(0).dropLast(1).toInt() to get(2).dropLast(1).toInt()
    }

    override suspend fun solve1(input: Pair<Int, Int>): Long {
        var current = 20151125L
        (1..7000).forEach { i ->
            var y = i
            var x = 1
            while (y > 0) {
                if (x == input.second && y == input.first) return current
                current = (current * 252533L) % 33554393L
                y--
                x++
            }
        }
        return 0
    }

    override suspend fun solve2(input: Pair<Int, Int>) = 0
}