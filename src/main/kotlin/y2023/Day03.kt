package y2023

import Day
import utils.findPoints
import utils.toCharMatrix

class Day03 : Day<Array<CharArray>>(2023, 3) {

    override fun List<String>.parse() = toCharMatrix()

    override fun solve1(input: Array<CharArray>): Int {
        val nrs = input.getPartNumbers()
        return nrs.filter { it.adjacent }.sumOf { it.nr }
    }

    override fun solve2(input: Array<CharArray>): Int {
        val nrs = input.getPartNumbers()
        val gears = input.findPoints('*')
        return gears.map { nrs.getAdjacentPartNumbers(it.x.toInt(), it.y.toInt()) }.filter { it.size == 2 }.sumOf { it[0].nr * it[1].nr  }
    }

    private data class Partnumber(val nr: Int, val x: Int, val y: Int, val adjacent: Boolean) {
        val length get() = nr.toString().length

        fun isAdjacent(x: Int, y: Int): Boolean {
            return x >= this.x - 1
                && x <= this.x + length
                && y >= this.y - 1
                && y <= this.y + 1
        }
    }

    private fun Array<CharArray>.getPartNumbers(): List<Partnumber> {
        val list = mutableListOf<Partnumber>()
        for (y in this[0].indices) {
            var x = 0
            while (x < size) {
                var nr = ""
                while (x < size && !this[x][y].isDigit()) {
                    x++
                }
                while (x < size && this[x][y].isDigit()) {
                    nr += this[x][y]
                    x++
                }
                if (nr.isNotBlank()) {
                    list += Partnumber(nr.toInt(), x - nr.length, y, partHasAdjacentSymbol(x - nr.length, y, nr.length))
                }
            }
        }
        return list
    }

    private fun Array<CharArray>.partHasAdjacentSymbol(x: Int, y: Int, length: Int): Boolean {
        for (xx in x - 1..x + length) {
            for (yy in y - 1..y + 1) {
                if (xx >= 0 && yy >= 0 && xx < size && yy < this[0].size) {
                    if (!this[xx][yy].isDigit() && this[xx][yy] != '.') {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun List<Partnumber>.getAdjacentPartNumbers(x: Int, y: Int) = filter { it.isAdjacent(x, y) }
}