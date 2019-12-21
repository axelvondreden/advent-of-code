package y2019

import Day
import Utils

class Day16 : Day() {

    override val input = Utils.readString(2019, 16).map { it.toString().toInt() }.toIntArray()

    override fun solve1(): String {
        var txt = input.copyOf()
        repeat(100) {
            txt = translate(txt)
        }
        return txt.joinToString(separator = "").substring(0..7)
    }

    override fun solve2(): String {
        val input2 = mutableListOf<Int>()
        for (i in 0 until 10000) {
            input2.addAll(input.toList())
        }
        val first7 = input.joinToString("").substring(0..6).toInt()
        var final = input2.toIntArray()
        repeat(100) {
            final = translate2(final)
        }
        return final.joinToString(separator = "").substring(first7..first7 + 7)
    }

    private fun translate2(input: IntArray): IntArray {
        val output = IntArray(input.size)
        output[output.size - 1] = input[output.size - 1]
        for (i in output.size - 2 downTo 0) {
            output[i] = (output[i + 1] + input[i]) % 10
        }
        return output
    }

    private fun translate(input: IntArray) = input.mapIndexed { index, _ -> multiplySignal(input, getPattern(index)) }.toIntArray()

    private fun multiplySignal(input: IntArray, pattern: IntArray) = input.indices.sumBy { input[it] * pattern[it % pattern.size] }.toString().last().toString().toInt()

    private fun getPattern(index: Int): IntArray {
        var newPattern = emptyList<Int>().toMutableList()
        for (i in intArrayOf(0, 1, 0, -1)) {
            repeat(index + 1) {
                newPattern.add(i)
            }
        }
        val last = newPattern.first()
        newPattern = newPattern.drop(1).toMutableList()
        newPattern.add(last)
        return newPattern.toIntArray()
    }
}