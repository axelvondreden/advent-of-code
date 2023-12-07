package y2021

import Day

class Day14 : Day<List<String>>(2021, 14) {

    private lateinit var insertions: Map<String, String>

    override fun List<String>.parse() = this.also { list ->
        insertions = list.subList(2, list.size).associate {
            val split = it.split(" -> ")
            split[0] to split[0][0] + split[1]
        }
    }

    override fun solve1(input: List<String>): Int {
        var template = input[0]
        repeat(10) {
            template = template.doInsertions()
        }
        val grouped = template.groupBy { it }.mapValues { it.value.size }
        return grouped.values.max() - grouped.values.min()
    }

    override fun solve2(input: List<String>): Int {
        var template = input[0]
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