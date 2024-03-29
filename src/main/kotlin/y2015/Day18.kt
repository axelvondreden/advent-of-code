package y2015

import DayViz
import androidx.compose.ui.graphics.Color
import runner.compose.Tile
import runner.compose.VizGrid

class Day18 : DayViz<CharArray, VizGrid>(2015, 18) {

    override suspend fun List<String>.parse() = joinToString("").toCharArray()

    override suspend fun solve1(input: CharArray): Int {
        Grid(100, 100, input.clone()).let { grid ->
            repeat(100) { grid.tick() }
            return grid.onCells()
        }
    }

    override suspend fun solve2(input: CharArray): Int {
        input[0] = '#'
        input[100 - 1] = '#'
        input[100 * 100 - 100] = '#'
        input[100 * 100 - 1] = '#'
        Grid(100, 100, input).let { grid ->
            repeat(100) { grid.tick(true) }
            return grid.onCells()
        }
    }

    override fun initViz1(input: CharArray) = VizGrid(width = 100, height = 100).apply {
        info["Lights"] = "0"
    }

    override fun initViz2(input: CharArray) = VizGrid(width = 100, height = 100).apply {
        info["Lights"] = "0"
    }

    override val vizDelay: Long
        get() = 80

    override suspend fun solve1Visualized(input: CharArray, onProgress: suspend (VizGrid) -> Unit): Int {
        Grid(100, 100, input.clone()).let { grid ->
            var viz = VizGrid(0.0, 100, 100)
            grid.paint(viz)
            onProgress(viz)
            repeat(100) {
                grid.tick()
                viz = VizGrid((it + 1).toDouble() / 100, 100, 100)
                grid.paint(viz)
                onProgress(viz)
            }
            return grid.onCells()
        }
    }

    override suspend fun solve2Visualized(input: CharArray, onProgress: suspend (VizGrid) -> Unit): Int {
        input[0] = '#'
        input[100 - 1] = '#'
        input[100 * 100 - 100] = '#'
        input[100 * 100 - 1] = '#'
        Grid(100, 100, input.clone()).let { grid ->
            var viz = VizGrid(0.0, 100, 100)
            grid.paint(viz)
            onProgress(viz)
            repeat(100) {
                grid.tick(true)
                viz = VizGrid((it + 1).toDouble() / 100, 100, 100)
                grid.paint(viz)
                onProgress(viz)
            }
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

        fun paint(viz: VizGrid) {
            viz.info["Lights"] = onCells().toString()
            viz.grid(0, 0, Array(100) { x -> Array(100) { y -> Tile(backgroundColor = if (isOn(x, y)) Color.White else Color(0xFF121212)) } })
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