package y2023

import Day

class Day03 : Day(2023, 3) {

    override val input = readCharMatrix()

    override fun solve1(): Int {
        val nrs = getPartNumbers()
        return nrs.filter { it.adjacent }.sumOf { it.nr }
    }

    override fun solve2() = 0

    private data class Partnumber(val nr: Int, val x: Int, val y: Int, val adjacent: Boolean) {
        val length get() = nr.toString().length
    }

    private fun getPartNumbers(): List<Partnumber> {
        val list = mutableListOf<Partnumber>()
        for (y in input[0].indices) {
            var x = 0
            while (x < input.size) {
                var nr = ""
                while (x < input.size && !input[x][y].isDigit()) {
                    x++
                }
                while (x < input.size && input[x][y].isDigit()) {
                    nr += input[x][y]
                    x++
                }
                if (nr.isNotBlank()) {
                    list += Partnumber(nr.toInt(), x - nr.length, y, partHasAdjacentSymbol(x - nr.length, y, nr.length))
                }
            }
        }
        return list
    }

    private fun partHasAdjacentSymbol(x: Int, y: Int, length: Int): Boolean {
        for (xx in x - 1..x + 1 + length) {
            for (yy in y - 1..y + 1) {
                if (xx >= 0 && yy >= 0 && xx < input.size && yy < input[0].size) {
                    if (!input[xx][yy].isDigit() && input[xx][yy] != '.') {
                        return true
                    }
                }
            }
        }
        return false
    }
}