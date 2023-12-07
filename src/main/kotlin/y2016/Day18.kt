package y2016

import Day


class Day18 : Day<Any?>(2016, 18) {

    override val input = readString()

    override fun solve1(input: List<String>): Int {
        var currentRow = input
        val map = mutableListOf<String>()
        repeat(40) {
            map.add(currentRow)
            currentRow = currentRow.nextRow()
        }
        return map.sumOf { row -> row.count { it == '.' } }
    }

    override fun solve2(input: List<String>): Int {
        var currentRow = input
        val map = mutableListOf<String>()
        repeat(400000) {
            map.add(currentRow)
            currentRow = currentRow.nextRow()
        }
        return map.sumOf { row -> row.count { it == '.' } }
    }

    private fun String.nextRow(): String {
        val new = StringBuilder()
        while (new.length < length) {
            val index = new.length
            val left = index > 0 && get(index - 1) == '^'
            val center = get(index) == '^'
            val right = index < length - 1 && get(index + 1) == '^'
            new.append(if (isTrap(left, center, right)) '^' else '.')
        }
        return new.toString()
    }

    private fun isTrap(left: Boolean, center: Boolean, right: Boolean): Boolean {
        if (left && center && !right) return true
        if (center && right && !left) return true
        if (left && !center && !right) return true
        if (!left && !center && right) return true
        return false
    }
}