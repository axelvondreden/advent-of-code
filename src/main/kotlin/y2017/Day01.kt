package y2017

import Day

class Day01 : Day<Any?>(2017, 1) {

    override val input = readString()

    override fun solve1(input: List<String>) = captcha(1)

    override fun solve2(input: List<String>) = captcha(input.length / 2)

    private fun captcha(offset: Int) = input.indices.filter {
        input[it] == input[(it + offset) % input.length]
    }.sumOf { input[it].toString().toInt() }
}