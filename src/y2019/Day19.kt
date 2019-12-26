package y2019

import Day
import Utils

class Day19 : Day() {

    override val input = Utils.readLongArray(2019, 19)

    override fun solve1(): Int {
        var count = 0
        for (y in 0L..49) {
            for (x in 0L..49) {
                if (IntCodeComputer(input.copyOf()).addInput(x).addInput(y).run().value == 1L) {
                    count++
                }
            }
        }
        return count
    }

    override fun solve2(): Long {
        val list = mutableListOf<Pair<Long, Long>>()
        var i = 0L
        while (true) {
            (0..i).filter { IntCodeComputer(input.copyOf()).addInput(it).addInput(i).run().value == 1L }
                .forEach { list.add(Pair(it, i)) }
            val search = find(list)
            if (search > 0) {
                return search
            }
            i++
        }
    }

    private fun find(list: List<Pair<Long, Long>>): Long {
        val maxY = list.map { it.second }.max()!!
        val minX = list.filter { it.second == maxY }.map { it.first }.min()!!
        for (y in maxY - 99..maxY) {
            for (x in minX..minX + 99) {
                if (list.none { it.first == x && it.second == y }) {
                    return -1
                }
            }
        }
        return (minX * 10000) + (maxY - 99)
    }
}