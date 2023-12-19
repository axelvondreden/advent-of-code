package y2021

import Day
import utils.Point
import utils.product
import utils.toCharMatrix

class Day09 : Day<List<List<Int>>>(2021, 9) {

    override suspend fun List<String>.parse() = toCharMatrix().map { row -> row.map { it.toString().toInt() } }

    override suspend fun solve1(input: List<List<Int>>): Int {
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

    override suspend fun solve2(input: List<List<Int>>): Int {
        val basins = mutableListOf<Set<Point>>()
        for (x in input.indices) {
            for (y in input[x].indices) {
                val point = Point(x, y)
                val value = input[x][y]
                if (value < 9 && point !in basins.flatten()) {
                    val set = mutableSetOf<Point>()
                    input.getLowerNeighbours(input, set, point)
                    basins += set
                }
            }
        }
        return basins.map { it.size }.sortedDescending().take(3).product()
    }

    private fun List<List<Int>>.getLowerNeighbours(input: List<List<Int>>, set: MutableSet<Point>, point: Point) {
        set += point
        point.neighbours(input).forEach {
            if (this[it.x.toInt()][it.y.toInt()] < 9 && it !in set) {
                getLowerNeighbours(input, set, it)
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

    private fun Point.neighbours(input: List<List<Int>>): Set<Point> {
        val set = mutableSetOf<Point>()
        input.getOrNull(x.toInt())?.getOrNull(y.toInt() - 1)?.let { set += Point(x, y - 1) }
        input.getOrNull(x.toInt())?.getOrNull(y.toInt() + 1)?.let { set += Point(x, y + 1) }
        input.getOrNull(x.toInt() - 1)?.getOrNull(y.toInt())?.let { set += Point(x - 1, y) }
        input.getOrNull(x.toInt() + 1)?.getOrNull(y.toInt())?.let { set += Point(x + 1, y) }
        return set
    }
}