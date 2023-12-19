package y2017

import Day

class Day01 : Day<String>(2017, 1) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String) = input.captcha(1)

    override suspend fun solve2(input: String) = input.captcha(input.length / 2)

    private fun String.captcha(offset: Int) = indices.filter {
        this[it] == this[(it + offset) % length]
    }.sumOf { this[it].toString().toInt() }
}