package y2020

import Day

class Day08 : Day<List<Day08.Instruction>>(2020, 8) {

    override suspend fun List<String>.parse() = toInstructions()

    override suspend fun solve1(input: List<Instruction>) = Accumulator().apply { runWithoutLoop(input) }.acc

    override suspend fun solve2(input: List<Instruction>): Int {
        val switchMax = input.count { it.cmd == "nop" || it.cmd == "jmp" }
        var switchIndex = 0
        while (switchIndex < switchMax) {
            val acc = Accumulator()
            val switchedInput = mutableListOf<Instruction>()
            var cmdCount = 0
            input.forEach { instruction ->
                if (instruction.cmd == "jmp" || instruction.cmd == "nop") {
                    if (cmdCount != switchIndex) switchedInput.add(instruction)
                    else switchedInput.add(Instruction(if (instruction.cmd == "nop") "jmp" else "nop", instruction.arg))
                    cmdCount++
                } else {
                    switchedInput.add(instruction)
                }
            }
            try {
                return acc.apply { run(switchedInput, 10000) }.acc
            } catch (e: IllegalAccessException) {
                switchIndex++
            }
        }
        return 0
    }

    private fun List<String>.toInstructions() = map {
        val split = it.split(" ")
        Instruction(split[0], split[1].toInt())
    }

    data class Instruction(val cmd: String, val arg: Int)

    private class Accumulator {
        var acc = 0

        fun run(instr: List<Instruction>, max: Int = Int.MAX_VALUE) {
            var index = 0
            var count = 0
            while (index in instr.indices) {
                if (count >= max) throw IllegalAccessException()
                index += runSingle(instr[index])
                count++
            }
        }

        fun runWithoutLoop(instr: List<Instruction>) {
            var index = 0
            val visited = mutableSetOf<Int>()
            while (index in instr.indices) {
                if (index in visited) return else visited.add(index)
                index += runSingle(instr[index])
            }
        }

        private fun runSingle(inst: Instruction): Int {
            when (inst.cmd) {
                "acc" -> acc += inst.arg
                "jmp" -> return inst.arg
                "nop" -> {}
            }
            return 1
        }
    }
}