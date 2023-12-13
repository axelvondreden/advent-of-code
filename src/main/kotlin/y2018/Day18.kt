package y2018

import Day
import utils.findPoints
import utils.toCharMatrix

class Day18 : Day<Array<CharArray>>(2018, 18) {

    override fun List<String>.parse(): Array<CharArray> = toCharMatrix()

    override fun solve1(input: Array<CharArray>): Int {
        var map = input
        repeat(10) { map = map.step() }
        return map.findPoints('#').size * map.findPoints('|').size
    }

    override fun solve2(input: Array<CharArray>): Int {
        var map = input
        repeat(1000000000) { map = map.step() }
        return map.findPoints('#').size * map.findPoints('|').size
    }

    private fun Array<CharArray>.step() = Array(size) { x ->
        CharArray(this[x].size) { y ->
            val neighbours = neighbours(x, y)
            when (this[x][y]) {
                '.' -> if (neighbours.count { it == '|' } >= 3) '|' else '.'
                '|' -> if (neighbours.count { it == '#' } >= 3) '#' else '|'
                '#' -> if (neighbours.any { it == '#' } && neighbours.any { it == '|' }) '#' else '.'
                else -> error("Big Fail")
            }
        }
    }

    private fun Array<CharArray>.neighbours(x: Int, y: Int): List<Char> {
        val list = mutableListOf<Char>()
        for (xx in x - 1..x + 1) {
            for (yy in y - 1..y + 1) {
                if (xx in indices && yy in this[0].indices && !(xx == x && yy == y)) {
                    list.add(this[xx][yy])
                }
            }
        }
        return list
    }
}