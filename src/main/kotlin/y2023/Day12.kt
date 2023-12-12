package y2023

import Day
import utils.permute

class Day12 : Day<List<Day12.Row>>(2023, 12) {

    override fun List<String>.parse() = map { line ->
        val s = line.split(" ")
        Row(s[0].toCharArray(), s[1].split(",").map { it.toInt() })
    }

    override fun solve1(input: List<Row>): Any {
        println("#.#.###".toCharArray().matches(listOf(1, 1, 3)))
        println("#....######..#####.".toCharArray().matches(listOf(1, 6, 5)))
        println(".###.##....#".toCharArray().matches(listOf(3, 2, 1)))
        val p = getPermutations(2)
        val p2 = getPermutations(3)
        val p3 = getPermutations(5)
        return 0
    }

    override fun solve2(input: List<Row>): Any = 0

    private fun sumOfValidConstellations(rows: List<Row>): Int {
        val cache = mutableMapOf<Int, Set<String>>()
        val unknownIndices = row.springs.withIndex().filter { it.value == '?' }.map { it.index }
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
        return sizes.withIndex().all { it.value == groups[it.index] }
    }
}