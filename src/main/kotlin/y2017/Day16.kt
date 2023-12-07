package y2017

import Day

class Day16 : Day<List<String>>(2017, 16) {

    override val input = readString().split(",").toInstructions()

    private val programs = CharArray(16) { 'a' + it }

    override fun solve1(input: List<String>): String {
        val progs = programs.copyOf()
        input.forEach {
            it.move(progs)
        }
        return progs.joinToString("")
    }

    override fun solve2(input: List<String>): String {
        val progs = programs.copyOf()
        repeat(1_000_000_000) {
            input.forEach {
                it.move(progs)
            }
        }
        return progs.joinToString("")
    }

    private fun List<String>.toInstructions() = map {
        val inst = it[0]
        val cmd = it.substring(1)
        when (inst) {
            's' -> Instruction.Spin(cmd.toInt())
            'x' -> with(cmd.split("/")) { Instruction.Exchange(get(0).toInt(), get(1).toInt()) }
            'p' -> Instruction.Partner(cmd[0], cmd[2])
            else -> error("wrong instruction")
        }
    }

    sealed class Instruction {
        abstract fun move(programs: CharArray)

        class Spin(private val amount: Int) : Instruction() {
            override fun move(programs: CharArray) {
                val last = programs.copyOfRange(programs.size - amount, programs.size)
                (programs.lastIndex downTo amount).forEach { programs[it] = programs[it - amount] }
                last.forEachIndexed { index, c -> programs[index] = c }
            }
        }

        class Exchange(private val pos1: Int, private val pos2: Int) : Instruction() {
            override fun move(programs: CharArray) {
                val temp = programs[pos1]
                programs[pos1] = programs[pos2]
                programs[pos2] = temp
            }
        }

        class Partner(private val prog1: Char, private val prog2: Char) : Instruction() {
            override fun move(programs: CharArray) {
                val tempIndex = programs.indexOf(prog2)
                programs[programs.indexOf(prog1)] = prog2
                programs[tempIndex] = prog1
            }
        }
    }
}
