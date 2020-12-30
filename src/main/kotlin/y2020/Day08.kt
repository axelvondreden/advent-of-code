package y2020

import Day

class Day08 : Day(2020, 8) {

    override val input = parseInstructions(readStrings())

    override fun solve1(): Int {
        val acc = Accumulator()
        acc.runWithoutLoop(input)
        return acc.acc
    }

    override fun solve2(): Int {
        val switchMax = input.count { it.cmd == "nop" || it.cmd == "jmp" }
        var switchIndex = 0
        while (switchIndex < switchMax) {
            val acc = Accumulator()
            val switchedInput = mutableListOf<Instruction>()
            var cmdCount = 0
            input.forEach { instruction ->
                if (instruction.cmd == "jmp" || instruction.cmd == "nop") {
                    if (cmdCount != switchIndex) {
                        switchedInput.add(instruction)
                    } else {
                        switchedInput.add(Instruction(if (instruction.cmd == "nop") "jmp" else "nop", instruction.arg))
                    }
                    cmdCount++
                } else {
                    switchedInput.add(instruction)
                }
            }
            try {
                acc.run(switchedInput, 10000)
                return acc.acc
            } catch (e: IllegalAccessException) {
                switchIndex++
            }
        }
        return 0
    }

    private fun parseInstructions(input: List<String>) = input.map {
        val split = it.split(" ")
        Instruction(split[0], split[1].toInt())
    }

    data class Instruction(val cmd: String, val arg: Int)

    class Accumulator {
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
                "acc" -> {
                    acc += inst.arg
                }
                "jmp" -> return inst.arg
                "nop" -> {}
            }
            return 1
        }
    }
}