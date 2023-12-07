package y2017

import Day

class Day15 : Day<List<String>>(2017, 15) {

    override val input = readStrings()

    override fun solve1(input: List<String>): Int {
        val genA = Generator(input[0].split(" ")[4].toLong(), genAFactor)
        val genB = Generator(input[1].split(" ")[4].toLong(), genBFactor)
        return (0 until 40_000_000).count {
            val genANr = genA.next()
            val genBNr = genB.next()
            divisorList.all { genANr % it == genBNr % it }
        }
    }

    override fun solve2(input: List<String>): Int {
        val genA = Generator(input[0].split(" ")[4].toLong(), genAFactor) { it % 4 == 0L }
        val genB = Generator(input[1].split(" ")[4].toLong(), genBFactor) { it % 8 == 0L }
        return (0 until 5_000_000).count {
            val genANr = genA.next()
            val genBNr = genB.next()
            divisorList.all { genANr % it == genBNr % it }
        }
    }

    private companion object {
        private const val genAFactor = 16807L
        private const val genBFactor = 48271L
        private const val divisor = 2147483647L
        private val divisorList = generateSequence(2) { it * 2 }.take(16).toList()
    }

    private class Generator(private var nr: Long, private val factor: Long, private val validator: (Long) -> Boolean = { true }) {
        fun next(): Long {
            do { nr = (nr * factor) % divisor } while (!validator.invoke(nr))
            return nr
        }
    }
}
