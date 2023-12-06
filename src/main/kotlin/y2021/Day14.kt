package y2021

import Day

class Day14 : Day(2021, 14) {

    override val input = readStrings()

    private val original = input[0]

    private val insertions = input.subList(2, input.size).associate {
        val split = it.split(" -> ")
        split[0] to split[0][0] + split[1]
    }

    override fun solve1(): Int {
        var template = original
        repeat(10) {
            template = template.doInsertions()
        }
        val grouped = template.groupBy { it }.mapValues { it.value.size }
        return grouped.values.max() - grouped.values.min()
    }

    override fun solve2(): Int {
        var template = original
        repeat(40) {
            println(it)
            template = template.doInsertions()
        }
        val grouped = template.groupBy { it }.mapValues { it.value.size }
        return grouped.values.max() - grouped.values.min()
    }

    private fun String.doInsertions(): String {
        val result = StringBuilder()
        windowed(2).forEach {
            result.append(insertions[it] ?: it)
        }
        return result.append(last()).toString()
    }
}