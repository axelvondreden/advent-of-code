package y2019

import Day
import utils.toLongArray

class Day02 : Day<LongArray>(2019, 2) {

    override fun List<String>.parse() = first().toLongArray().also { it[1] = 12 }.also { it[2] = 2 }

    override fun solve1(input: LongArray): Long {
        val register = input.copyOf()
        return IntCodeComputer(register).run().register[0]
    }

    override fun solve2(input: LongArray): Long {
        (0..99L).forEach { i ->
            (0..99L).forEach { j ->
                val register = input.copyOf().apply {
                    set(1, i)
                    set(2, j)
                }
                val result = IntCodeComputer(register).run()
                if (result.register[0] == 19690720L) return 100 * i + j
            }
        }
        return 0
    }
}