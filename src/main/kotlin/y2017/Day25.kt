package y2017

import Day

class Day25 : Day<List<String>>(2017, 25) {

    override fun List<String>.parse() = this

    private val start = input[0].split(" ")[3][0]
    private val steps = input[1].split(" ")[5].toInt()

    private val tape = mutableMapOf<Int, Boolean>()
    private var cursor = 0

    private val states = input.parseStates()

    override fun solve1(input: List<String>): Int {
        var count = 0
        var state = states[start]
        while (count < steps) {
            count++
            state = state!!.func.invoke(tape.getOrDefault(cursor, false))
        }
        return tape.values.count { it }
    }

    override fun solve2(input: List<String>) = 0

    private fun List<String>.parseStates(): Map<Char, State> = drop(3).chunked(10).associate { chunk ->
        val c = chunk[0].split(" ")[2][0]
        val writeIf0 = chunk[2].dropLast(1).last() == '1'
        val moveIf0 = if (chunk[3].split(" ").last() == "right.") 1 else -1
        val stateIf0 = chunk[4].split(" ").last()[0]
        val writeIf1 = chunk[6].dropLast(1).last() == '1'
        val moveIf1 = if (chunk[7].split(" ").last() == "right.") 1 else -1
        val stateIf1 = chunk[8].split(" ").last()[0]
        c to State {
            if (it) {
                tape[cursor] = writeIf1
                cursor += moveIf1
                states[stateIf1]!!
            } else {
                tape[cursor] = writeIf0
                cursor += moveIf0
                states[stateIf0]!!
            }
        }
    }

    private class State(val func: (Boolean) -> State)
}
