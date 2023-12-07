package y2020

import Day


class Day25 : Day<Any?>(2020, 25) {

    override val input = readStrings()

    private val cardPublic = input[0].toLong()
    private val doorPublic = input[1].toLong()

    override fun solve1(input: List<String>): Long {
        val loopSize = generateSequence(1L) { (it * 7) % 20201227 }.indexOf(cardPublic)
        return generateSequence(1L) { (it * doorPublic) % 20201227 }.drop(loopSize).first()
    }

    override fun solve2(input: List<String>) = 0
}