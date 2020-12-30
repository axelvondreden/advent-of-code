package y2020

import Day

class Day10 : Day(2020, 10) {

    override val input = readStrings().map { it.toInt() }

    override fun solve1(): Int {
        val deviceJoltage = input.maxOrNull()!! + 3
        var diff1 = 0
        var diff3 = 0
        var current = 0
        var index = 0
        val list = input.plus(deviceJoltage).sorted()
        while (index < list.size) {
            if (list[index] - current == 1) {
                diff1++
            } else if (list[index] - current == 3) {
                diff3++
            }
            current = list[index]
            index++
        }
        return diff1 * diff3
    }

    override fun solve2(): Long {
        val list = input.plus(0).sorted()
        val paths = Array(list.size) { 0L }
        paths[0] = 1L
        for (i in 1 until paths.size) {
            for (j in 1 .. 3) {
                val prev = i - j
                if (prev >= 0 && (list[i] - list[prev]) <= 3) {
                    paths[i] += paths[i - j]
                }
            }
        }
        return paths.last()
    }
}