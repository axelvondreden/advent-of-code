package y2023

import Day
import utils.permute

class Day12 : Day<List<Day12.Row>>(2023, 12) {

    override fun List<String>.parse() = map { line ->
        val s = line.split(" ")
        Row(s[0].toCharArray(), s[1].split(",").map { it.toInt() })
    }

    override fun solve1(input: List<Row>) = sumOfValidConstellations(input)

    override fun solve2(input: List<Row>): Any = 0

    private fun sumOfValidConstellations(rows: List<Row>): Int {
        val cache = mutableMapOf<Int, Set<String>>()
        var sum = 0
        rows.forEachIndexed { index1, row ->
            println("${index1 + 1} / ${rows.size}")
            val unknownIndices = row.springs.withIndex().filter { it.value == '?' }.map { it.index }
            val replacements = cache.getOrPut(unknownIndices.size) { getPermutations(unknownIndices.size) }
            val replaced = replacements.count { replacement ->
                val replaced = row.springs.apply {
                    unknownIndices.forEachIndexed { index, springIndex ->
                        set(springIndex, replacement[index])
                    }
                }
                replaced.matches(row.groups)
            }
            sum += replaced
        }
        return sum
    }

    private fun getPermutations(length: Int): Set<String> {
        val list = mutableSetOf<String>()
        var broken = 0
        while (broken <= length) {
            val initial = List(length) { if (it < broken) '#' else '.' }
            val perms = initial.permute()
            list.addAll(perms.map { it.joinToString("") })
            broken++
        }
        return list
    }

    data class Row(val springs: CharArray, val groups: List<Int>)

    private fun CharArray.matches(groups: List<Int>): Boolean {
        val sizes = mutableListOf<Int>()
        var currentSize = 0
        for (c in this) {
            if (c == '#') {
                currentSize++
            } else if (currentSize > 0) {
                sizes += currentSize
                currentSize = 0
            }
        }
        if (currentSize > 0) {
            sizes += currentSize
        }
        return sizes.size == groups.size && sizes.withIndex().all { it.value == groups[it.index] }
    }
}