package y2020

import Day

class Day10 : Day<List<Int>>(2020, 10) {

    override suspend fun List<String>.parse() = map { it.toInt() }

    override suspend fun solve1(input: List<Int>): Int {
        val deviceJoltage = input.maxOrNull()!! + 3
        var diff1 = 0
        var diff3 = 0
        var current = 0
        var index = 0
        val list = input.plus(deviceJoltage).sorted()
        while (index < list.size) {
            if (list[index] - current == 1) diff1++
            else if (list[index] - current == 3) diff3++
            current = list[index]
            index++
        }
        return diff1 * diff3
    }

    override suspend fun solve2(input: List<Int>): Long {
        val list = input.plus(0).sorted()
        val paths = Array(list.size) { 0L }
        paths[0] = 1L
        (1 until paths.size).forEach { i ->
            (1 .. 3).forEach { j ->
                val prev = i - j
                if (prev >= 0 && (list[i] - list[prev]) <= 3) {
                    paths[i] += paths[i - j]
                }
            }
        }
        return paths.last()
    }
}