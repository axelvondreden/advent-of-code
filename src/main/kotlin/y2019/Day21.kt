package y2019

import Day
import utils.toLongArray

class Day21 : Day<LongArray>(2019, 21) {

    override fun List<String>.parse() = first().toLongArray()

    private val inst1 = "NOT A J\nNOT B T\nOR T J\nNOT C T\nOR T J\nAND D J\n"
    private val inst2 = "NOT F J\nOR E J\nOR H J\nAND D J\nNOT C T\nAND T J\nNOT D T\nOR B T\nOR E T\nNOT T T\nOR T J\nNOT A T\nOR T J\n"

    override fun solve1(input: LongArray) = input.run("${inst1}WALK\n".map { it.code.toLong() }.toLongArray())

    override fun solve2(input: LongArray) = input.run("${inst2}RUN\n".map { it.code.toLong() }.toLongArray())

    fun LongArray.run(instructions: LongArray): Int {
        val comp = IntCodeComputer(copyOf()).withInputs(instructions)
        val out = mutableListOf<Char>()
        var o = comp.run()
        var last = 0L
        while (!o.halted) {
            last = o.value
            out.add(o.value.toInt().toChar())
            o = comp.run()
        }
        return last.toInt()
    }
}