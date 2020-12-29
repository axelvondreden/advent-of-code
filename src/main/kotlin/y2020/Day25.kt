package y2020

import Day
import utils.IO


class Day25 : Day() {

    override val input = IO.readStrings(2020, 25)

    private val cardPublic = input[0].toLong()
    private val doorPublic = input[1].toLong()

    override fun solve1(): Long {
        val loopSize = generateSequence(1L) { (it * 7) % 20201227 }.indexOf(cardPublic)
        return generateSequence(1L) { (it * doorPublic) % 20201227 }.drop(loopSize).first()
    }

    override fun solve2() = 0
}