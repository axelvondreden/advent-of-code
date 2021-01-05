package y2015

import Day
import utils.product

class Day24 : Day(2015, 24) {

    override val input = readInts().map { it.toLong() }

    override fun solve1(): Long {
        (1..input.size).forEach { i ->
            val set = mutableSetOf<Set<Long>>()
            getGroups(input.sum() / 3, emptySet(), set, i)
            if (set.isNotEmpty()) {
                val min = set.map { it.size }.minOrNull()
                return set.filter { it.size == min }.map { it.product() }.minOrNull()!!
            }
        }
        return 0
    }

    override fun solve2(): Long {
        (1..input.size).forEach { i ->
            val set = mutableSetOf<Set<Long>>()
            getGroups(input.sum() / 4, emptySet(), set, i)
            if (set.isNotEmpty()) {
                val min = set.map { it.size }.minOrNull()
                return set.filter { it.size == min }.map { it.product() }.minOrNull()!!
            }
        }
        return 0
    }

    private fun getGroups(size: Long, used: Set<Long>, out: MutableSet<Set<Long>>, elements: Int) {
        input.filter { it !in used && used.sum() + it <= size }.forEach { w ->
            val newSet = used.plus(w).toMutableSet()
            if (newSet.sum() == size) out.add(newSet)
            else if (newSet.size < elements) getGroups(size, newSet, out, elements)
        }
    }
}