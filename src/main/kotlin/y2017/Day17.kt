package y2017

import Day

class Day17 : Day(2017, 17) {

    override val input = readString().toInt()

    override fun solve1(): Int {
        val buffer = fillBuffer(2017)
        return buffer[buffer.indexOf(2017) + 1]
    }

    override fun solve2(): Int {
        val buffer = fillBuffer(50000000)
        return buffer[buffer.indexOf(0) + 1]
    }

    private fun fillBuffer(size: Int): ArrayDeque<Int> {
        val buffer = ArrayDeque<Int>(size).apply { add(0) }
        var currentPosition = 0
        var counter = 1
        while (counter <= size) {
            currentPosition = (currentPosition + input + 1) % buffer.size
            buffer.add(currentPosition, counter)
            counter++
        }
        return buffer
    }
}
