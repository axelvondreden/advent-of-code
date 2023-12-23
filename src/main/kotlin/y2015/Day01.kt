package y2015

import Day
import androidx.compose.ui.graphics.Color
import runner.compose.VizGrid

class Day01 : Day<String>(2015, 1) {

    override suspend fun List<String>.parse() = this[0]

    override suspend fun solve1(input: String) = input.count { it == '(' } - input.count { it == ')' }

    override suspend fun solve2(input: String): Int {
        var floor = 0
        for ((index, c) in input.withIndex()) {
            when (c) {
                '(' -> floor++
                ')' -> floor--
            }
            if (floor < 0) return index + 1
        }
        return 0
    }

    override fun initViz1(input: String) = VizGrid(width = 50, height = 10).apply {
        info["Level"] = "0"
        border(0, 3, Color.Yellow)
        text(1, 3, input.take(49))
    }

    override fun initViz2(input: String) = VizGrid(width = 50, height = 10).apply {
        info["Level"] = "0"
        info["Position"] = "0"
        border(0, 3, Color.Yellow)
        text(1, 3, input.take(49))
    }

    override suspend fun solve1Visualized(input: String, onProgress: suspend (VizGrid) -> Unit): Int {
        var lvl = 0
        for ((index, c) in input.withIndex()) {
            val viz = VizGrid((index + 1).toDouble() / input.length, width = 50, height = 10).apply {
                text(1, 3, input.substring(index + 1).take(49))
            }

            if (c == '(') {
                lvl++
                viz.text(0, 3, c.toString(), borderColor = Color.Green)
            } else {
                lvl--
                viz.text(0, 3, c.toString(), borderColor = Color.Red)
            }
            viz.info["Level"] = lvl.toString()

            onProgress(viz)
        }
        return lvl
    }

    override suspend fun solve2Visualized(input: String, onProgress: suspend (VizGrid) -> Unit): Any {
        var lvl = 0
        for ((index, c) in input.withIndex()) {
            val viz = VizGrid((index + 1).toDouble() / input.length, width = 50, height = 10).apply {
                text(1, 3, input.substring(index + 1).take(49))
            }
            when (c) {
                '(' -> {
                    lvl++
                    viz.text(0, 3, c.toString(), borderColor = Color.Green)
                }

                ')' -> {
                    lvl--
                    viz.text(0, 3, c.toString(), borderColor = Color.Red)
                }
            }
            viz.info["Level"] = lvl.toString()
            viz.info["Position"] = (index + 1).toString()
            onProgress(viz)
            if (lvl < 0) return index + 1
        }
        return 0
    }
}