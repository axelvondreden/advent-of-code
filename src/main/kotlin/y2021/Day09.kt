package y2021

import Day
import utils.Point
import utils.product

class Day09 : Day(2021, 9) {

    override val input = readCharMatrix().map { row -> row.map { it.toString().toInt() } }

    override fun solve1(): Int {
        var sum = 0
        for (x in input.indices) {
            for (y in input[x].indices) {
                val value = input[x][y]
                if (input.neighbourValues(x, y).all { it > value }) {
                    sum += value + 1
                }
            }
        }
        return sum
    }

    override fun solve2(): Int {
        val basins = mutableListOf<Set<Point>>()
        for (x in input.indices) {
            for (y in input[x].indices) {
                val point = Point(x, y)
                val value = input[x][y]
                if (value < 9 && point !in basins.flatten()) {
                    val set = mutableSetOf<Point>()
                    getLowerNeighbours(set, point)
                    basins += set
                }
            }
        }
        return basins.map { it.size }.sortedDescending().take(3).product()
    }

    private fun getLowerNeighbours(set: MutableSet<Point>, point: Point) {
        set += point
        point.neighbours().forEach {
            if (input[it.x.toInt()][it.y.toInt()] < 9 && it !in set) {
                getLowerNeighbours(set, it)
            }
        }
    }

    private fun List<List<Int>>.neighbourValues(x: Int, y: Int): List<Int> {
        val list = mutableListOf<Int>()
        getOrNull(x)?.getOrNull(y - 1)?.let { list += it }
        getOrNull(x)?.getOrNull(y + 1)?.let { list += it }
        getOrNull(x - 1)?.getOrNull(y)?.let { list += it }
        getOrNull(x + 1)?.getOrNull(y)?.let { list += it }
        return list
    }

    private fun Point.neighbours(): Set<Point> {
        val set = mutableSetOf<Point>()
        input.getOrNull(x.toInt())?.getOrNull(y.toInt() - 1)?.let { set += Point(x, y - 1) }
        input.getOrNull(x.toInt())?.getOrNull(y.toInt() + 1)?.let { set += Point(x, y + 1) }
        input.getOrNull(x.toInt() - 1)?.getOrNull(y.toInt())?.let { set += Point(x - 1, y) }
        input.getOrNull(x.toInt() + 1)?.getOrNull(y.toInt())?.let { set += Point(x + 1, y) }
        return set
    }
}