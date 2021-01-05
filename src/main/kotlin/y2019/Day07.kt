package y2019

import Day

class Day07 : Day(2019, 7) {

    override val input = readLongArray()

    override fun solve1() = listOf(0L, 1, 2, 3, 4).toAmps().map { list ->
        list.map { Amplifier(input.copyOf(), it, false) }
    }.map { runAmplifiers(it) }.maxOrNull()!!

    override fun solve2() = listOf(5L, 6, 7, 8, 9).toAmps().map { list ->
        list.map { Amplifier(input.copyOf(), it, true) }
    }.map { runAmplifiersFeedback(it) }.maxOrNull()!!

    private fun runAmplifiers(amps: List<Amplifier>): Long {
        var input = 0L
        (0..4).forEach { input = amps[it].runRegister(input).value }
        return input
    }

    private fun runAmplifiersFeedback(amps: List<Amplifier>): Long {
        var input = 0L
        var i = 0
        var result = 0L
        do {
            input = amps[i].runRegister(input).value
            if (i < 4) {
                i++
            } else {
                i = 0
                result = input
            }
        } while (input != -1L)
        return result
    }

    private fun List<Long>.toAmps(): Set<List<Long>> = when {
        isEmpty() -> setOf()
        size == 1 -> setOf(listOf(get(0)))
        else -> drop(1).toAmps().flatMap { sublist -> (0..sublist.size).map { i -> sublist.insert(i, get(0)) } }.toSet()
    }

    private fun List<Long>.insert(index: Int, element: Long): List<Long> = when (index) {
        0 -> listOf(element) + this
        size -> this + element
        else -> dropLast(size - index) + element + drop(index)
    }

    private class Amplifier(register: LongArray, phase: Long, outputZeroes: Boolean) {

        private val comp = IntCodeComputer(register, outputZeroes = outputZeroes).addInput(phase)

        fun runRegister(input: Long) = comp.addInput(input).run()
    }
}