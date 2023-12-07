package y2022

import Day

class Day10 : Day<List<String>>(2022, 10) {

    override fun List<String>.parse() = this

    private val rows = 6
    private val columns = 40

    override fun solve1(input: List<String>) = input.asSequence()
        .toInstructions()
        .toStates()
        .filter { it.pc >= 20 }
        .filter { (it.pc - 20) % 40 == 0 }
        .sumOf { it.pc * it.x }

    override fun solve2(input: List<String>): String {
        val res = input.asSequence()
            .toInstructions()
            .toStates()
            .withIndex()
            .fold(StringBuilder(rows * (columns + 1))) { sb, (i, state) ->
                val crtX = i % columns
                if (crtX == 0 && i != 0) sb.append('\n')
                if (crtX - state.x in -1..1) sb.append('#')
                else sb.append(' ')
            }
            .toString()
        return if (res == expect.substring(1)) "PLEFULPB" else ""
    }

    private fun Sequence<Instruction>.toStates() = sequence {
        var current = State(1, 1)
        for (instruction in this@toStates) {
            yield(current)
            current = State(current.pc + 1, instruction.apply(current.x))
        }
    }

    private data class State(val pc: Int, val x: Int)

    private fun Sequence<String>.toInstructions() = map { Instruction.parse(it) }.flatMap {
        List(it.cycles - 1) { Instruction.Noop } + it
    }

    private fun Instruction.apply(target: Int) = when (this) {
        is Instruction.AddX -> target + x
        Instruction.Noop -> target
    }

    private sealed class Instruction(val cycles: Int) {
        data class AddX(val x: Int) : Instruction(2)
        object Noop : Instruction(1)

        companion object {
            fun parse(line: String) = if (line == "noop") {
                Noop
            } else if (line.startsWith("addx")) {
                AddX(line.split(" ").last().toInt())
            } else {
                error("no instruction for $line")
            }
        }
    }

    private companion object {
        private const val expect = """
###  #    #### #### #  # #    ###  ###  
#  # #    #    #    #  # #    #  # #  # 
#  # #    ###  ###  #  # #    #  # ###  
###  #    #    #    #  # #    ###  #  # 
#    #    #    #    #  # #    #    #  # 
#    #### #### #     ##  #### #    ###  """
    }
}