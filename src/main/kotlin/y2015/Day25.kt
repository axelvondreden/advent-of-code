package y2015

import Day

class Day25 : Day() {

    override val input = Pair(2947, 3029)

    override fun solve1(): Long {
        var current = 20151125L
        for (i in 1..7000) {
            var y = i
            var x = 1
            while (y > 0) {
                if (x == input.second && y == input.first) {
                    return current
                }
                current = (current * 252533L) % 33554393L
                y--
                x++
            }
        }
        return 0
    }

    override fun solve2() {

    }
}