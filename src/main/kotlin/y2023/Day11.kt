package y2023

import Day
import utils.*

class Day11 : Day<Array<CharArray>>(2023, 11) {

    override fun List<String>.parse() = toCharMatrix()

    override fun solve1(input: Array<CharArray>): Int {
        val emptyColumns = input.indices.filter { x -> input[x].all { it == '.' } }
        val emptyRows = input[0].indices.filter { y -> input.indices.all { x -> input[x][y] == '.' } }
        var expanded = input
        emptyRows.sortedDescending().forEach {
            expanded = expanded.insertRow(it, '.')
        }
        emptyColumns.sortedDescending().forEach {
            expanded = expanded.insertColumn(it, '.')
        }
        val galaxies = expanded.findPoints('#')
        return 0
    }

    override fun solve2(input: Array<CharArray>): Int = 0
}