package y2021

import Day

class Day11 : Day<List<String>>(2021, 11) {

    override val input = readCharMatrix().map { chars -> chars.map { it.digitToInt() }.toIntArray() }

    override fun solve1(input: List<String>): Int {
        var sum = 0
        val copy = input.toTypedArray()
        repeat(100) {
            sum += copy.simulate()
        }
        return sum
    }

    override fun solve2(input: List<String>): Int {
        var round = 0
        val copy = input.toTypedArray()
        var flashes = 0
        while (flashes < copy.sumOf { it.size }) {
            flashes = copy.simulate()
            round++
        }
        return round + 100
    }

    private fun Array<IntArray>.simulate(): Int {
        val flashed = mutableSetOf<Pair<Int, Int>>()
        for (x in this.indices) {
            for (y in this[0].indices) {
                this[x][y]++
            }
        }
        while (flashed.size < sumOf { ints -> ints.count { it > 9 } }) {
            for (x in this.indices) {
                for (y in this[0].indices) {
                    if (this[x][y] > 9 && (x to y) !in flashed) {
                        flashed += x to y
                        for (xx in -1..1) {
                            for (yy in -1..1) {
                                if (xx != 0 || yy != 0) {
                                    val newX = x + xx
                                    val newY = y + yy
                                    if (newX in this.indices && newY in this[0].indices) {
                                        this[newX][newY]++
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        flashed.forEach {
            this[it.first][it.second] = 0
        }
        return flashed.size
    }
}