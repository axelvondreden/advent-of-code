package y2017

import Day

class Day17 : Day(2017, 17) {

    override val input = readString().toInt()

    override fun solve1(): Int {
        val buffer = ArrayDeque<Int>(2018).apply { add(0) }
        var currentPosition = 0
        var counter = 1
        while (counter <= 2017) {
            currentPosition = (currentPosition + input + 1) % buffer.size
            buffer.add(currentPosition, counter)
            counter++
        }
        return buffer[buffer.indexOf(2017) + 1]
    }

    override fun solve2(): Int {
        val buffer = ArrayDeque<Int>(50_000_000).apply { add(0) }
        var currentPosition = 0
        var counter = 1
        while (counter <= 50_000_000) {
            currentPosition = (currentPosition + input + 1) % buffer.size
            buffer.add(currentPosition, counter)
            counter++
            if (counter % 100000 == 0) println(counter / 50_000_000.0)
        }
        return buffer[buffer.indexOf(0) + 1]
    }
}
