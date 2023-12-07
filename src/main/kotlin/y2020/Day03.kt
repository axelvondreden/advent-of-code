package y2020

import Day
import utils.Point
import utils.toCharMatrix

class Day03 : Day<Array<CharArray>>(2020, 3) {

    override fun List<String>.parse() = toCharMatrix()

    override fun solve1(input: Array<CharArray>) = input.countTrees(3, 1)

    override fun solve2(input: Array<CharArray>) = input.countTrees(1, 1) *
            input.countTrees(3, 1) * input.countTrees(5, 1) *
            input.countTrees(7, 1) * input.countTrees(1, 2)

    private fun Array<CharArray>.countTrees(right: Int, down: Int): Long {
        var position = Point(0, 0)
        var trees = 0L
        while (position.y < this[0].size) {
            if (this[position.x.toInt() % size][position.y.toInt()] == '#') {
                trees++
            }
            position += Point(right, down)
        }
        return trees
    }
}