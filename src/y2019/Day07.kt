package y2019

import Day
import Utils

class Day07 : Day() {

    override val input = Utils.readLongArray(2019, 7)

    override fun solve1() = listOf(0L, 1, 2, 3, 4).toAmps().map {
        it.map { Amplifier(input.copyOf(), it) }
    }.map { runAmplifiers(it) }.max()!!

    override fun solve2() = listOf(5L, 6, 7, 8, 9).toAmps().map {
        it.map { Amplifier(input.copyOf(), it) }
    }.map { runAmplifiersFeedback(it) }.max()!!

    private fun runAmplifiers(amps: List<Amplifier>): Long {
        var input = 0L
        for (i in 0..4) {
            input = amps[i].runRegister(input)
        }
        return input
    }

    private fun runAmplifiersFeedback(amps: List<Amplifier>): Long {
        var input = 0L
        var i = 0
        var result = 0L
        do {
            input = amps[i].runRegister(input, false)
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

    class Amplifier(register: LongArray, phase: Long) {

        private val comp = IntCodeComputer(register).addInput(phase)

        fun runRegister(input: Long, ignoreReturn0: Boolean = true) = comp.addInput(input).run(ignoreReturn0)
    }
}