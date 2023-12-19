package y2019

import Day
import utils.toLongArray

class Day25 : Day<LongArray>(2019, 25) {

    override suspend fun List<String>.parse() = first().toLongArray()

    override suspend fun solve1(input: LongArray): Int {
        val comp = IntCodeComputer(input)
        var last9 = ""
        while (true) {
            val reply = comp.run()
            val c = reply.value.toInt().toChar()
            last9 = last9.takeLast(8) + c
            print(c)
            if (last9 == "Command?\n") {
                (readlnOrNull() + '\n').forEach {
                    comp.addInput(it.code.toLong())
                }
            }
        }
    }

    override suspend fun solve2(input: LongArray) = 0
}