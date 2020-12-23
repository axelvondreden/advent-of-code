package y2020

import Day
import utils.IO

class Day09 : Day() {

    override val input = IO.readStrings(2020, 9).map { it.toLong() }

    private var wrongNumber = 0L

    override fun solve1(): Long {
        wrongNumber = getFirstWrongNumber(input, 25)
        return wrongNumber
    }

    override fun solve2(): Long {
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
        if (sum == target) {
            return subList(start, start + indexDelta)
        }
        return emptyList()
    }

    private fun getFirstWrongNumber(list: List<Long>, preambleSize: Int): Long {
        val prevList = list.subList(0, preambleSize).toMutableList()
        var index = preambleSize
        while (index < list.size) {
            if (!prevList.canSum(list[index])) {
                return list[index]
            }
            prevList.removeAt(0)
            prevList.add(list[index])
            index++
        }
        return 0
    }

    private fun List<Long>.canSum(nr: Long): Boolean {
        forEach { i ->
            forEach { j ->
                if (i != j && i + j == nr) {
                    return true
                }
            }
        }
        return false
    }
}