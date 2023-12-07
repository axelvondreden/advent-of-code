package y2019

import Day

class Day25 : Day<List<String>>(2019, 25) {

    override val input = readLongArray()

    override fun solve1(input: List<String>): Int {
        val comp = IntCodeComputer(input)
        var last9 = ""
        while (true) {
            val reply = comp.run()
            val c = reply.value.toInt().toChar()
            last9 = last9.takeLast(8) + c
            print(c)
            if (last9 == "Command?\n") {
                (readLine() + '\n').forEach {
                    comp.addInput(it.code.toLong())
                }
            }
        }
    }

    override fun solve2(input: List<String>) = 0
}