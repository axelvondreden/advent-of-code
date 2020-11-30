package y2019

import Day
import Utils

class Day02 : Day() {

    override val input = Utils.readLongArray(2019, 2).also { it[1] = 12 }.also { it[2] = 2 }

    override fun solve1(): Long {
        val register = input.copyOf()
        IntCodeComputer(register).run()
        return register[0]
    }

    override fun solve2(): Long {
        for (i in 0..99L) {
            for (j in 0..99L) {
                val register = input.copyOf()
                IntCodeComputer(register).run()
                if (register[0] == 19690720L) {
                    return 100 * i + j
                }
            }
        }
        return 0
    }
}