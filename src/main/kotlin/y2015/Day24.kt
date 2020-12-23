package y2015

import Day
import utils.IO
import utils.product

class Day24 : Day() {

    override val input = IO.readInts(2015, 24).map { it.toLong() }

    override fun solve1(): Long {
        for (i in 1..input.size) {
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
        for (i in 1..input.size) {
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
        for (w in input.filter { it !in used && used.sum() + it <= size }) {
            val newSet = mutableSetOf<Long>()
            newSet.addAll(used)
            newSet.add(w)
            if (newSet.sum() == size) {
                out.add(newSet)
            } else if (newSet.size < elements) {
                getGroups(size, newSet, out, elements)
            }
        }
    }
}