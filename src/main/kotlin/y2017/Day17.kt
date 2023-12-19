package y2017

import Day

class Day17 : Day<Int>(2017, 17) {

    override suspend fun List<String>.parse() = first().toInt()

    override suspend fun solve1(input: Int): Int {
        val buffer = input.fillBuffer(2017)
        return buffer[buffer.indexOf(2017) + 1]
    }

    override suspend fun solve2(input: Int): Int {
        val buffer = input.fillBuffer(50000000)
        return buffer[buffer.indexOf(0) + 1]
    }

    private fun Int.fillBuffer(size: Int): ArrayDeque<Int> {
        val buffer = ArrayDeque<Int>(size).apply { add(0) }
        var currentPosition = 0
        var counter = 1
        while (counter <= size) {
            currentPosition = (currentPosition + this + 1) % buffer.size
            buffer.add(currentPosition, counter)
            counter++
        }
        return buffer
    }
}
