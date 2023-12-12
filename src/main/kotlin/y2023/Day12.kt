package y2023

import Day
import utils.permute

class Day12 : Day<List<Day12.Row>>(2023, 12) {

    override fun List<String>.parse() = map { line ->
        val s = line.split(" ")
        Row(s[0].toCharArray(), s[1].split(",").map { it.toInt() })
    }

    override fun solve1(input: List<Row>): Int {
        return sumOfValidConstellations(input)
    }

    override fun solve2(input: List<Row>): Any = 0

    private fun sumOfValidConstellations(rows: List<Row>): Int {
        val cache = mutableMapOf<Int, Set<String>>()
        var index = 1
        return rows.sumOf { row ->
            println("$index / ${rows.size}")
            index++
            val unknownGroups = row.springs.getUnknownGroups()
            countValidConstellations(row, unknownGroups)
        }
    }

    fun countValidConstellations(row: Row, indicesToReplace: List<IntRange>): Int {
        return replacementHelper(row, indicesToReplace, 0)
    }

    fun replacementHelper(row: Row, indicesToReplace: List<IntRange>, currentIndex: Int): Int {
        // Base case - if currentIndex is the size of indicesToReplace, validate the configuration
        if (currentIndex == indicesToReplace.size) {
            return if (row.springs.matches(row.groups)) 1 else 0
        }

        var validCombinations = 0

        val replacements = listOf('#', '.')
        val replacementRange = indicesToReplace[currentIndex] // Assume that this IntRange includes the end index.

        // Traverse each replacement character
        replacements.forEach { replacement ->
            // Ensure all '?' within the current range are replaced with the current replacement character
            for (index in replacementRange) {
                row.springs[index] = replacement
            }

            // Move to the next '?'
            validCombinations += replacementHelper(row, indicesToReplace, currentIndex + 1)

            // Replace the springs back to '?'
            for (index in replacementRange) {
                row.springs[index] = '?'
            }
        }

        return validCombinations
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

    private fun CharArray.getUnknownGroups(): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        var start: Int? = null

        for (i in indices) {
            if (this[i] == '?') {
                if (start == null) {
                    start = i
                }
            } else if (start != null) {
                ranges.add(start..<i)
                start = null
            }
        }

        start?.let { ranges.add(it..lastIndex) }
        return ranges
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