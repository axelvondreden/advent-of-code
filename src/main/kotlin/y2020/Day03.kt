package y2020

import Day
import utils.Point

class Day03 : Day(2020, 3) {

    override val input = readCharMatrix()

    override fun solve1() = countTrees(3, 1)

    override fun solve2() = countTrees(1, 1) * countTrees(3, 1) * countTrees(5, 1) *
            countTrees(7, 1) * countTrees(1, 2)

    private fun countTrees(right: Int, down: Int): Long {
        var position = Point(0, 0)
        var trees = 0L
        while (position.y < input[0].size) {
            if (input[position.x % input.size][position.y] == '#') {
                trees++
            }
            position += Point(right, down)
        }
        return trees
    }
}