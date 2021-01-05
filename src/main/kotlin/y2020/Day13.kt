package y2020

import Day


class Day13 : Day(2020, 13) {

    override val input = readStrings()

    override fun solve1(): Int {
        val earliest = input[0].toInt()
        val busses = input[1].split(",").filter { it != "x" }.map { it.toInt() }
        val earliestBus = busses.minByOrNull { it - (earliest % it) }!!
        return earliestBus * (earliestBus - (earliest % earliestBus))
    }

    override fun solve2(): Long {
        val busses = input[1].split(",").map { it.toIntOrNull() ?: 1 }
        var multiplier = busses[0].toLong()
        var time = busses[0].toLong()
        var index = 1
        while (true) {
            val id = busses[index].toLong()
            if ((time + index) % id == 0L) {
                if (++index >= busses.size) return time
                multiplier *= id
                continue
            }
            time += multiplier
        }
    }
}