package y2016

import Day


class Day20 : Day<List<LongRange>>(2016, 20) {

    override suspend fun List<String>.parse() = map {
        val split = it.split("-")
        split[0].toLong()..split[1].toLong()
    }.sortedBy { it.first }

    override suspend fun solve1(input: List<LongRange>): Long {
        var ip = 0L
        while (ip <= 4294967295L) {
            val range = input.firstOrNull { ip in it }
            if (range != null) ip = range.last + 1 else return ip
        }
        return 0
    }

    override suspend fun solve2(input: List<LongRange>): Int {
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