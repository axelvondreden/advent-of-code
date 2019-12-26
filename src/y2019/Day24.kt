package y2019

import Day
import Utils
import copy
import kotlin.math.pow

class Day24 : Day() {

    override val input = Utils.readCharMatrix(2019, 24)

    override fun solve1(): Int {
        val states = mutableSetOf(input.toFlatString())
        var new = step(input)
        while (!states.contains(new.toFlatString())) {
            states.add(new.toFlatString())
            new = step(new)
        }
        return new.toFlatString().mapIndexed { i, c -> if (c == '#') 2.toDouble().pow(i).toInt() else 0 }.sum()
    }

    override fun solve2(): Int {
        return 0
    }

    private fun step(map: Array<CharArray>): Array<CharArray> {
        val new = map.copy()
        for (y in new[0].indices) {
            for (x in new.indices) {
                val n = map.countNeighbors(x, y)
                if (map[x][y] == '#') {
                    new[x][y] = if (n == 1) '#' else '.'
                } else {
                    new[x][y] = if (n == 1 || n == 2) '#' else '.'
                }
            }
        }
        return new
    }

    private fun Array<CharArray>.countNeighbors(x: Int, y: Int): Int {
        var count = 0
        if (x > 0) count += if (get(x - 1)[y] == '#') 1 else 0
        if (y > 0) count += if (get(x)[y - 1] == '#') 1 else 0
        if (x < size - 1) count += if (get(x + 1)[y] == '#') 1 else 0
        if (y < get(0).size - 1) count += if (get(x)[y + 1] == '#') 1 else 0
        return count
    }

    private fun Array<CharArray>.toFlatString(): String {
        var ret = ""
        for (y in get(0).indices) {
            for (x in indices) {
                ret += get(x)[y]
            }
        }
        return ret
    }
}