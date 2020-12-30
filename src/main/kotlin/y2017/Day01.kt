package y2017

import Day

class Day01 : Day(2017, 1) {

    override val input = readString()

    override fun solve1() = captcha(1)

    override fun solve2() = captcha(input.length / 2)

    private fun captcha(offset: Int) = input.indices.filter { input[it] == input[(it + offset) % input.length] }.sumBy { input[it].toString().toInt() }
}