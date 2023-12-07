package y2019

import Day
import utils.toLongArray

class Day19 : Day<LongArray>(2019, 19) {

    override fun List<String>.parse() = first().toLongArray()

    override fun solve1(input: LongArray) = (0L..49).sumOf { y ->
        (0L..49).filter { IntCodeComputer(input.copyOf()).addInput(it).addInput(y).run().value == 1L }.size
    }

    override fun solve2(input: LongArray): Long {
        val list = mutableListOf<Pair<Long, Long>>()
        var i = 0L
        while (true) {
            (0..i).filter { IntCodeComputer(input.copyOf()).addInput(it).addInput(i).run().value == 1L }.forEach { list.add(Pair(it, i)) }
            val search = find(list)
            if (search > 0) return search
            i++
        }
    }

    private fun find(list: List<Pair<Long, Long>>): Long {
        val maxY = list.maxOf { it.second }
        val minX = list.filter { it.second == maxY }.minOf { it.first }
        (maxY - 99..maxY).forEach { y ->
            (minX..minX + 99).forEach { x ->
                if (list.none { it.first == x && it.second == y }) return -1
            }
        }
        return (minX * 10000) + (maxY - 99)
    }
}