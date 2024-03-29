package y2019

import Day

class Day16 : Day<IntArray>(2019, 16) {

    override suspend fun List<String>.parse() = first().map { it.toString().toInt() }.toIntArray()

    override suspend fun solve1(input: IntArray): String {
        var txt = input.copyOf()
        repeat(100) {
            txt = translate(txt)
        }
        return txt.joinToString(separator = "").substring(0..7)
    }

    override suspend fun solve2(input: IntArray): String {
        val input2 = mutableListOf<Int>()
        repeat(10000) {
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
        (output.size - 2 downTo 0).forEach {
            output[it] = (output[it + 1] + input[it]) % 10
        }
        return output
    }

    private fun translate(input: IntArray) = input.mapIndexed { index, _ -> multiplySignal(input, getPattern(index)) }.toIntArray()

    private fun multiplySignal(input: IntArray, pattern: IntArray) = input.indices.sumOf { input[it] * pattern[it % pattern.size] }.toString().last().toString().toInt()

    private fun getPattern(index: Int): IntArray {
        var newPattern = emptyList<Int>().toMutableList()
        intArrayOf(0, 1, 0, -1).forEach { i ->
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