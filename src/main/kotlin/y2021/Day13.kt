package y2021

import Day
import utils.Point
import utils.toMapString

class Day13 : Day<List<String>>(2021, 13) {

    override fun List<String>.parse() = this

    override fun solve1(input: List<String>): Int {
        val dots = input.takeWhile { it.isNotBlank() }.map { Point(it) }.toSet()
        val folds = input.dropWhile { it.isNotBlank() }.drop(1).map { it.split(" ")[2] }
        return dots.fold(folds[0]).size
    }

    override fun solve2(input: List<String>): String {
        val dots = input.takeWhile { it.isNotBlank() }.map { Point(it) }.toSet()
        val folds = input.dropWhile { it.isNotBlank() }.drop(1).map { it.split(" ")[2] }
        var d = dots.toSet()
        folds.forEach { d = d.fold(it) }
        val a = Array(d.maxOf { it.x + 1 }.toInt()) { BooleanArray(d.maxOf { it.y + 1 }.toInt()) }
        d.forEach { a[it.x.toInt()][it.y.toInt()] = true }
        return if (a.toMapString() == control) "RPCKFBLR" else ""
    }

    private fun Set<Point>.fold(fold: String): Set<Point> {
        val x = if (fold[0] == 'x') fold.substring(2).toInt() else 0
        val y = if (fold[0] == 'y') fold.substring(2).toInt() else 0
        return map {
            if (x > 0) {
                if (it.x > x) {
                    val dif = it.x - x
                    Point(it.x - dif * 2, it.y)
                } else it
            } else {
                if (it.y > y) {
                    val dif = it.y - y
                    Point(it.x, it.y - dif * 2)
                } else it
            }
        }.toSet()
    }

    private companion object {
        private const val control = """###..###...##..#..#.####.###..#....###.
#..#.#..#.#..#.#.#..#....#..#.#....#..#
#..#.#..#.#....##...###..###..#....#..#
###..###..#....#.#..#....#..#.#....###.
#.#..#....#..#.#.#..#....#..#.#....#.#.
#..#.#.....##..#..#.#....###..####.#..#
"""
    }
}