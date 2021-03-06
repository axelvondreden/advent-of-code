package y2015

import Day

class Day18 : Day(2015, 18) {

    override val input = readStrings().joinToString("").toCharArray()

    override fun solve1(): Int {
        Grid(100, 100, input.clone()).let { grid ->
            repeat(100) { grid.tick() }
            return grid.onCells()
        }
    }

    override fun solve2(): Int {
        input[0] = '#'
        input[100 - 1] = '#'
        input[100 * 100 - 100] = '#'
        input[100 * 100 - 1] = '#'
        Grid(100, 100, input).let { grid ->
            repeat(100) { grid.tick(true) }
            return grid.onCells()
        }
    }

    private data class Grid(var width: Int, var height: Int, var cells: CharArray) {

        fun onCells() = cells.fold(0) { total, cell -> if (cell == '#') total + 1 else total }

        private fun getCell(x: Int, y: Int) = cells[width * x + y]

        private fun isOn(x: Int, y: Int) = getCell(x, y) == '#'

        private fun isInGrid(x: Int, y: Int) = x in 0 until width && y in 0 until height

        private fun getNeighboursCount(x: Int, y: Int): Int {
            var count = if (isOn(x, y)) -1 else 0
            (0..2).forEach { yy ->
                repeat((0..2).filter {
                    isInGrid(x + it - 1, y + yy - 1) && isOn(
                        x + it - 1,
                        y + yy - 1
                    )
                }.size) { count++ }
            }
            return count
        }

        fun tick(part2: Boolean = false) {
            val cells = CharArray(width * height) { '.' }
            if (part2) {
                cells[0] = '#'
                cells[width - 1] = '#'
                cells[width * height - 1] = '#'
                cells[width * height - width] = '#'
            }

            (0 until height).forEach { y ->
                (0 until width).forEach { x ->
                    val onLightsCount = getNeighboursCount(x, y)
                    if ((isOn(x, y) && onLightsCount in 2..3) || onLightsCount == 3) cells[width * x + y] = '#'
                }
            }
            this.cells = cells
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Grid
            if (width != other.width) return false
            if (height != other.height) return false
            if (!cells.contentEquals(other.cells)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = width
            result = 31 * result + height
            result = 31 * result + cells.contentHashCode()
            return result
        }
    }

}