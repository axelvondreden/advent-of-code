package y2019

import Day
import Utils

class Day21 : Day() {

    override val input = Utils.readLongArray(2019, 21)

    override fun solve1(): Int {
        val instructions = ("NOT A J\nNOT B T\nOR T J\nNOT C T\nOR T J\nAND D J\nWALK\n").toCharArray().map { it.toLong() }.toLongArray()
        val comp = IntCodeComputer(input.copyOf()).withInputs(instructions)
        var o = comp.run(false)
        var last = 0L
        while (o != -999L) {
            last = o
            o = comp.run(false)
        }
        return last.toInt()
    }

    override fun solve2(): Int {
        return 0
    }
}