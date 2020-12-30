package y2016

import Day


class Day20 : Day(2016, 20) {

    override val input = readStrings().map {
        val split = it.split("-")
        LongRange(split[0].toLong(), split[1].toLong())
    }.sortedBy { it.first }

    override fun solve1(): Long {
        var ip = 0L
        while (ip <= 4294967295L) {
            val range = input.firstOrNull { ip in it }
            if (range != null) {
                ip = range.last + 1
            } else {
                return ip
            }
        }
        return 0
    }

    override fun solve2(): Int {
        var ip = 0L
        var count = 0
        while (ip <= 4294967295L) {
            val range = input.firstOrNull { ip in it }
            if (range != null) {
                ip = range.last + 1
            } else {
                count++
                ip++
            }
        }
        return count
    }
}