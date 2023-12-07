package y2020

import Day

class Day09 : Day<List<String>>(2020, 9) {

    override fun List<String>.parse() = this.map { it.toLong() }

    private var wrongNumber = 0L

    override fun solve1(input: List<String>) = getFirstWrongNumber(input, 25).also { wrongNumber = it }

    override fun solve2(input: List<String>): Long {
        val subList = input.indices.map { input.findSubListToSum(it, wrongNumber) }.first { it.isNotEmpty() }
        return subList.minOrNull()!! + subList.maxOrNull()!!
    }

    private fun List<Long>.findSubListToSum(start: Int, target: Long): List<Long> {
        var sum = 0L
        var indexDelta = 0
        while (sum < target) {
            sum += get(start + indexDelta)
            indexDelta++
        }
        return if (sum == target) subList(start, start + indexDelta) else emptyList()
    }

    private fun getFirstWrongNumber(list: List<Long>, preambleSize: Int): Long {
        val prevList = list.subList(0, preambleSize).toMutableList()
        var index = preambleSize
        while (index < list.size) {
            if (!prevList.canSum(list[index])) return list[index]
            prevList.removeAt(0)
            prevList.add(list[index])
            index++
        }
        return 0
    }

    private fun List<Long>.canSum(nr: Long) = any { i -> any { j -> i != j && i + j == nr } }
}