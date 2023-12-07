package y2023

import Day

class Day01 : Day<Any?>(2023, 1) {

    override val input = readStrings()

    private val digits = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    override fun solve1(input: List<String>) = input.calc()

    override fun solve2(input: List<String>) = input.sumOf { line ->
        (line.firstDigit().toString() + line.lastDigit().toString()).toInt()
    }

    private fun List<String>.calc() = sumOf { line ->
        (line.first { it.isDigit() }.toString() + line.last { it.isDigit() }.toString()).toInt()
    }

    private fun String.firstDigit(): Int {
        var dIndex = indexOfFirst { it.isDigit() }
        if (dIndex < 0) dIndex = 1000
        val tIndex = digits.minOf {
            val index = indexOf(it.key)
            if (index < 0) 1000
            else index
        }
        if (dIndex < tIndex) return get(dIndex).digitToInt()
        return digits.minBy {
            val index = indexOf(it.key)
            if (index < 0) 1000
            else index
        }.value
    }

    private fun String.lastDigit(): Int {
        val dIndex = indexOfLast { it.isDigit() }
        val tIndex = digits.maxOf { lastIndexOf(it.key) }
        if (dIndex > tIndex) return get(dIndex).digitToInt()
        return digits.maxBy { lastIndexOf(it.key) }.value
    }
}