package y2015

import Day
import utils.product

class Day24 : Day<List<Long>>(2015, 24) {

    override suspend fun List<String>.parse() = map { it.toLong() }

    override suspend fun solve1(input: List<Long>): Long {
        (1..input.size).forEach { i ->
            val set = mutableSetOf<Set<Long>>()
            input.getGroups(input.sum() / 3, emptySet(), 0L, set, i)
            if (set.isNotEmpty()) {
                val min = set.minOfOrNull { it.size }
                return set.filter { it.size == min }.minOf { it.product() }
            }
        }
        return 0
    }

    override suspend fun solve2(input: List<Long>): Long {
        (1..input.size).forEach { i ->
            val set = mutableSetOf<Set<Long>>()
            input.getGroups(input.sum() / 4, emptySet(), 0L, set, i)
            if (set.isNotEmpty()) {
                val min = set.minOfOrNull { it.size }
                return set.filter { it.size == min }.minOf { it.product() }
            }
        }
        return 0
    }

    private fun List<Long>.getGroups(size: Long, used: Set<Long>, out: MutableSet<Set<Long>>, elements: Int) {
        filter { it !in used && used.sum() + it <= size }.forEach { w ->
            val newSet = used.plus(w).toMutableSet()
            if (newSet.sum() == size) out.add(newSet)
            else if (newSet.size < elements) getGroups(size, newSet, out, elements)
        }
    }

    private fun List<Long>.getGroups(size: Long, used: Set<Long>, usedSum: Long, out: MutableSet<Set<Long>>, elements: Int) {
        filter { it !in used && usedSum + it <= size }.forEach { w ->
            val newSet = used.plus(w).toMutableSet()
            val newSum = usedSum + w
            if (newSum == size) out.add(newSet)
            else if (newSet.size < elements) getGroups(size, newSet, newSum, out, elements)
        }
    }
}