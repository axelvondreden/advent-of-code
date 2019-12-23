package y2019

import Day
import Utils

class Day21 : Day() {

    override val input = Utils.readLongArray(2019, 21)

    private val inst1 = "NOT A J\nNOT B T\nOR T J\nNOT C T\nOR T J\nAND D J\n"
    private val inst2 = "NOT F J\nOR E J\nOR H J\nAND D J\nNOT C T\nAND T J\nNOT D T\nOR B T\nOR E T\nNOT T T\nOR T J\nNOT A T\nOR T J\n"

    override fun solve1() = run("${inst1}WALK\n".map { it.toLong() }.toLongArray())

    override fun solve2() = run("${inst2}RUN\n".map { it.toLong() }.toLongArray())

    fun run(instructions: LongArray): Int {
        val comp = IntCodeComputer(input.copyOf()).withInputs(instructions)
        val out = mutableListOf<Char>()
        var o = comp.run(false)
        var last = 0L
        while (o != -999L) {
            last = o
            out.add(o.toChar())
            o = comp.run(false)
        }
        if (last >= 128) {
            return last.toInt()
        }
        println(out.joinToString(""))
        return 0
    }
}