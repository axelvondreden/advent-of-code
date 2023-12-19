package y2018

import Day

class Day14 : Day<Int>(2018, 14) {

    override suspend fun List<String>.parse() = first().toInt()

    override suspend fun solve1(input: Int): String {
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

    override suspend fun solve2(input: Int): Int {
        val scores = mutableListOf(3, 7)
        var index1 = 0
        var index2 = 1
        while (true) {
            val rec1 = scores[index1]
            val rec2 = scores[index2]
            (rec1 + rec2).toString().map { it.toString().toInt() }.forEach {
                scores += it
                if (scores.check(input)) {
                    return scores.size - input.toString().length
                }
            }
            index1 += rec1 + 1
            index1 %= scores.size
            index2 += rec2 + 1
            index2 %= scores.size
        }
    }

    private fun List<Int>.check(input: Int): Boolean {
        return takeLast(input.toString().length).joinToString("") == input.toString()
    }
}