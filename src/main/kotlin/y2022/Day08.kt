package y2022

import Day
import kotlin.math.max

class Day08 : Day<Array<IntArray>>(2022, 8) {

    override fun List<String>.parse() = map { line -> line.map { it.digitToInt() }.toIntArray() }.toTypedArray()

    override fun solve1(input: Array<IntArray>) = input.indices.sumOf { x -> input[x].indices.count { y -> input.isVisible(x, y) } }

    override fun solve2(input: Array<IntArray>): Int {
        var max = 0
        for (x in input.indices) {
            for (y in input[0].indices) {
                max = max(max, input.getViewScore(x, y))
            }
        }
        return max
    }

    private fun Array<IntArray>.isVisible(x: Int, y: Int): Boolean {
        if (x == 0 || y == 0 || x == lastIndex || y == get(0).lastIndex) return true
        return isVisibleLeft(x, y) || isVisibleRight(x, y) || isVisibleTop(x, y) || isVisibleBottom(x, y)
    }

    private fun Array<IntArray>.getViewScore(x: Int, y: Int): Int {
        val height = get(x)[y]
        if (x == 0 || y == 0 || x == lastIndex || y == get(0).lastIndex) return 0
        var score = 1
        var temp = 0
        for (i in x - 1 downTo 0) {
            temp++
            if (get(i)[y] >= height) break
        }
        score *= temp
        temp = 0
        for (i in y - 1 downTo 0) {
            temp++
            if (get(x)[i] >= height) break
        }
        score *= temp
        temp = 0
        for (i in x + 1 until size) {
            temp++
            if (get(i)[y] >= height) break
        }
        score *= temp
        temp = 0
        for (i in y + 1 until get(0).size) {
            temp++
            if (get(x)[i] >= height) break
        }
        score *= temp
        return score
    }

    private fun Array<IntArray>.isVisibleLeft(x: Int, y: Int) = (0 until x).none { get(it)[y] >= get(x)[y] }
    private fun Array<IntArray>.isVisibleTop(x: Int, y: Int) = (0 until y).none { get(x)[it] >= get(x)[y] }
    private fun Array<IntArray>.isVisibleRight(x: Int, y: Int) = (lastIndex downTo  x + 1).none { get(it)[y] >= get(x)[y] }
    private fun Array<IntArray>.isVisibleBottom(x: Int, y: Int) = (get(0).lastIndex downTo  y + 1).none { get(x)[it] >= get(x)[y] }
}