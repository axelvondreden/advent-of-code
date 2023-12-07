package y2019

import Day

class Day13 : Day<Any?>(2019, 13) {

    override val input = readLongArray()

    override fun solve1(input: List<String>): Int {
        val comp = IntCodeComputer(input.copyOf())
        val set = emptySet<Triple<Long, Long, Long>>().toMutableSet()
        while (true) {
            val x = comp.run()
            if (x.halted) break
            val y = comp.run()
            val type = comp.run()
            set.add(Triple(x.value, y.value, type.value))
        }
        return set.count { it.third == 2L }
    }

    override fun solve2(input: List<String>): Long {
        var ballX = 0L
        var paddleX = 0L
        val comp2 = IntCodeComputer(input).withInputFunction { input(ballX, paddleX) }
        var score = 0L
        while (true) {
            val x = comp2.run()
            if (x.value == -1L) {
                comp2.run()
                score = comp2.run().value
                continue
            } else if (x.halted) {
                break
            }
            comp2.run()
            when (comp2.run().value) {
                3L -> paddleX = x.value
                4L -> ballX = x.value
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
        val maxX = set.maxOf { it.first }
        val maxY = set.maxOf { it.second }
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