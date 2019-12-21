package y2019

import Day
import Utils

class Day13 : Day() {

    override val input = Utils.readLongArray(2019, 13)

    override fun solve1(): Int {
        val comp = IntCodeComputer(input.copyOf())
        val set = emptySet<Triple<Long, Long, Long>>().toMutableSet()
        while (true) {
            val x = comp.run(false)
            if (x == -999L) {
                break
            }
            val y = comp.run(false)
            val type = comp.run(false)
            set.add(Triple(x, y, type))
        }
        return set.count { it.third == 2L }
    }

    override fun solve2(): Long {
        var ballX = 0L
        var paddleX = 0L
        val comp2 = IntCodeComputer(input).withInputFunction { input(ballX, paddleX) }
        var score = 0L
        while (true) {
            val x = comp2.run(false)
            if (x == -1L) {
                comp2.run(false)
                score = comp2.run(false)
                continue
            } else if (x == -999L) {
                break
            }
            comp2.run(false)
            when (comp2.run(false)) {
                3L -> paddleX = x
                4L -> ballX = x
            }
        }
        return score
    }

    private fun input(ballX: Long, paddleX: Long) = when {
        ballX == paddleX -> 0L
        ballX < paddleX -> -1L
        else -> 1L
    }

    private fun printGame(set: Set<Triple<Long, Long, Long>>) {
        val maxX = set.map { it.first }.max()!!
        val maxY = set.map { it.second }.max()!!
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                print(
                    when (set.first { it.first == x && it.second == y }.third) {
                        0L -> " "
                        1L -> "#"
                        2L -> "B"
                        3L -> "-"
                        4L -> "O"
                        else -> ""
                    }
                )
            }
            println()
        }
    }
}