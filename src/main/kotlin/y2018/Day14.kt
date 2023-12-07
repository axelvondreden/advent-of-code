package y2018

import Day

class Day14 : Day<List<String>>(2018, 14) {

    override fun List<String>.parse() = first().toInt()

    override fun solve1(input: List<String>): String {
        val scores = mutableListOf(3, 7)
        var index1 = 0
        var index2 = 1
        while (scores.size < input + 10) {
            val rec1 = scores[index1]
            val rec2 = scores[index2]
            scores.addAll((rec1 + rec2).toString().map { it.toString().toInt() })
            index1 += rec1 + 1
            index1 %= scores.size
            index2 += rec2 + 1
            index2 %= scores.size
        }
        return scores.takeLast(10).joinToString("")
    }

    override fun solve2(input: List<String>): Int {
        val scores = mutableListOf(3, 7)
        var index1 = 0
        var index2 = 1
        while (true) {
            val rec1 = scores[index1]
            val rec2 = scores[index2]
            (rec1 + rec2).toString().map { it.toString().toInt() }.forEach {
                scores += it
                if (scores.check()) {
                    return scores.size - input.toString().length
                }
            }
            index1 += rec1 + 1
            index1 %= scores.size
            index2 += rec2 + 1
            index2 %= scores.size
        }
    }

    private val checks = input.toString()

    private fun List<Int>.check(): Boolean {
        return takeLast(checks.length).joinToString("") == checks
    }
}