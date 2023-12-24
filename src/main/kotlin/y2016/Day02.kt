package y2016

import DayViz
import androidx.compose.ui.graphics.Color
import runner.compose.VizGrid

class Day02 : DayViz<List<String>, VizGrid>(2016, 2) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>) = input.joinToString("") { inp ->
        var position = 5
        inp.forEach { direction ->
            position = when (direction) {
                'U' -> handleUp1(position)
                'D' -> handleDown1(position)
                'L' -> handleLeft1(position)
                'R' -> handleRight1(position)
                else -> position
            }
        }
        position.toString()
    }

    override suspend fun solve2(input: List<String>) = input.joinToString("") { inp ->
        var position = 5
        inp.forEach { direction ->
            position = when (direction) {
                'U' -> handleUp2(position)
                'D' -> handleDown2(position)
                'L' -> handleLeft2(position)
                'R' -> handleRight2(position)
                else -> position
            }
        }
        when (position) {
            10 -> "A"
            11 -> "B"
            12 -> "C"
            13 -> "D"
            else -> position.toString()
        }
    }

    override fun initViz1(input: List<String>) = getViz(true, null, 0, input.size, null, null)

    override fun initViz2(input: List<String>) = getViz(false, null, 0, input.size, null, null)

    override suspend fun solve1Visualized(input: List<String>, onProgress: suspend (VizGrid) -> Unit): String {
        var code = ""
        input.forEachIndexed { inputLineIndex, inp ->
            var position = 5
            onProgress(getViz(true, 0.0, inputLineIndex + 1, input.size, code, inp.take(10)).apply { mark1(position) })
            inp.forEachIndexed { index, dir ->
                position = when (dir) {
                    'U' -> handleUp1(position)
                    'D' -> handleDown1(position)
                    'L' -> handleLeft1(position)
                    'R' -> handleRight1(position)
                    else -> position
                }
                onProgress(getViz(true, (index + 1).toDouble() / inp.length, inputLineIndex + 1, input.size, code, inp.drop(index).take(10)).apply { mark1(position) })
            }
            code += position
        }
        onProgress(getViz(true, 1.0, input.lastIndex, input.size, code, ""))
        return code
    }

    override suspend fun solve2Visualized(input: List<String>, onProgress: suspend (VizGrid) -> Unit): String {
        var code = ""
        input.forEachIndexed { inputLineIndex, inp ->
            var position = 5
            onProgress(getViz(false, 0.0, inputLineIndex + 1, input.size, code, inp.take(10)).apply { mark2(position) })
            inp.forEachIndexed { index, dir ->
                position = when (dir) {
                    'U' -> handleUp2(position)
                    'D' -> handleDown2(position)
                    'L' -> handleLeft2(position)
                    'R' -> handleRight2(position)
                    else -> position
                }
                onProgress(getViz(false, (index + 1).toDouble() / inp.length, inputLineIndex + 1, input.size, code, inp.drop(index).take(10)).apply { mark2(position) })
            }
            code += when (position) {
                10 -> "A"
                11 -> "B"
                12 -> "C"
                13 -> "D"
                else -> position
            }
        }
        onProgress(getViz(false, 1.0, input.lastIndex, input.size, code, ""))
        return code
    }

    private fun getViz(
        part1: Boolean,
        progress: Double?,
        currentLine: Int,
        lineCount: Int,
        code: String?,
        nextCommands: String?
    ) = VizGrid(progress, 10, if (part1) 6 else 8).apply {
        info["Instruction:"] = "$currentLine / $lineCount"
        fillInfo(code, nextCommands)
        if (part1) {
            text(4, 3, "1")
            text(5, 3, "2")
            text(6, 3, "3")
            text(4, 4, "4")
            text(5, 4, "5")
            text(6, 4, "6")
            text(4, 5, "7")
            text(5, 5, "8")
            text(6, 5, "9")
        } else {
            text(5, 3, "1")
            text(4, 4, "2")
            text(5, 4, "3")
            text(6, 4, "4")
            text(3, 5, "5")
            text(4, 5, "6")
            text(5, 5, "7")
            text(6, 5, "8")
            text(7, 5, "9")
            text(4, 6, "A")
            text(5, 6, "B")
            text(6, 6, "C")
            text(5, 7, "D")
        }
    }

    private fun VizGrid.fillInfo(code: String?, nextCommands: String?) {
        text(0, 0, "Code:")
        if (!code.isNullOrBlank()) {
            text(5, 0, code, borderColor = Color.Yellow)
        }
        if (!nextCommands.isNullOrBlank()) {
            text(0, 1, nextCommands.take(10))
            border(0, 1, Color.White)
        }
    }

    private fun VizGrid.mark1(pos: Int) {
        when (pos) {
            1 -> backgroundColor(4, 3, Color.LightGray)
            2 -> backgroundColor(5, 3, Color.LightGray)
            3 -> backgroundColor(6, 3, Color.LightGray)
            4 -> backgroundColor(4, 4, Color.LightGray)
            5 -> backgroundColor(5, 4, Color.LightGray)
            6 -> backgroundColor(6, 4, Color.LightGray)
            7 -> backgroundColor(4, 5, Color.LightGray)
            8 -> backgroundColor(5, 5, Color.LightGray)
            9 -> backgroundColor(6, 5, Color.LightGray)
        }
    }

    private fun VizGrid.mark2(pos: Int) {
        when (pos) {
            1 -> backgroundColor(5, 3, Color.LightGray)
            2 -> backgroundColor(4, 4, Color.LightGray)
            3 -> backgroundColor(5, 4, Color.LightGray)
            4 -> backgroundColor(6, 4, Color.LightGray)
            5 -> backgroundColor(3, 5, Color.LightGray)
            6 -> backgroundColor(4, 5, Color.LightGray)
            7 -> backgroundColor(5, 5, Color.LightGray)
            8 -> backgroundColor(6, 5, Color.LightGray)
            9 -> backgroundColor(7, 5, Color.LightGray)
            10 -> backgroundColor(4, 6, Color.LightGray)
            11 -> backgroundColor(5, 6, Color.LightGray)
            12 -> backgroundColor(6, 6, Color.LightGray)
            13 -> backgroundColor(5, 7, Color.LightGray)
        }
    }

    private fun handleUp1(position: Int) = if (position > 3) position - 3 else position

    private fun handleDown1(position: Int) = if (position < 7) position + 3 else position

    private fun handleLeft1(position: Int) = if (position !in restrictedLeftPositions) position - 1 else position

    private fun handleRight1(position: Int) = if (position !in restrictedRightPositions) position + 1 else position

    private fun handleUp2(position: Int) = when (position) {
        3, 13 -> position - 2
        6, 7, 8, 10, 11, 12 -> position - 4
        else -> position
    }

    private fun handleDown2(position: Int) = when (position) {
        1, 11 -> position + 2
        2, 3, 4, 6, 7, 8 -> position + 4
        else -> position
    }

    private fun handleLeft2(position: Int) = if (position !in intArrayOf(1, 2, 5, 10, 13)) position - 1 else position

    private fun handleRight2(position: Int) = if (position !in intArrayOf(1, 4, 9, 12, 13)) position + 1 else position

    companion object {
        private val restrictedLeftPositions = intArrayOf(1, 4, 7)
        private val restrictedRightPositions = intArrayOf(3, 6, 9)
    }
}
