package y2015

import Day
import androidx.compose.ui.graphics.Color
import runner.compose.Tile
import runner.compose.Viz
import runner.compose.tileText

class Day01 : Day<String>(2015, 1) {

    override fun List<String>.parse() = this[0]

    override fun solve1(input: String) = input.count { it == '(' } - input.count { it == ')' }

    override fun solve2(input: String): Int {
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

    override fun initViz1(input: String) = Viz().apply {
        text(0, 0, "Level:")
        text(6, 0, "0", borderColor = Color.LightGray)
        set(0, 5, Tile(borderColor = Color.Yellow))
        text(1, 5, input.take(29))
    }

    override fun initViz2(input: String) = Viz().apply {
        text(0, 0, "Level:")
        text(6, 0, "0", borderColor = Color.LightGray)
        text(0, 1, "Position:")
        text(9, 1, "0", borderColor = Color.LightGray)
        set(0, 5, Tile(borderColor = Color.Yellow))
        text(1, 5, input.take(29))
    }

    override suspend fun solve1Visualized(input: String, onProgress: suspend (Viz) -> Unit): Int {
        var lvl = 0
        for ((index, c) in input.withIndex()) {
            val viz = Viz((index + 1).toDouble() / input.length).apply {
                text(0, 0, "Level:")
                text(1, 5, input.substring(index + 1).take(29))
            }

            if (c == '(') {
                lvl++
                viz.set(0, 5, Tile(c, borderColor = Color.Green))
            } else {
                lvl--
                viz.set(0, 5, Tile(c, borderColor = Color.Red))
            }
            viz.set(
                6, 0, *tileText(
                    lvl.toString(), borderColor = when {
                        lvl < 0 -> Color.Red
                        lvl > 0 -> Color.Green
                        else -> Color.LightGray
                    }
                )
            )

            onProgress(viz)
        }
        return lvl
    }

    override suspend fun solve2Visualized(input: String, onProgress: suspend (Viz) -> Unit): Any {
        var floor = 0
        for ((index, c) in input.withIndex()) {
            val viz = Viz((index + 1).toDouble() / input.length).apply {
                set(0, 0, *tileText("Level:"))
                set(0, 1, *tileText("Position:"))
                set(1, 5, *tileText(input.substring(index + 1).take(29)))
            }
            when (c) {
                '(' -> {
                    floor++
                    viz.set(0, 5, Tile(c, borderColor = Color.Green))
                }

                ')' -> {
                    floor--
                    viz.set(0, 5, Tile(c, borderColor = Color.Red))
                }
            }
            viz.set(
                6, 0, *tileText(
                    floor.toString(), borderColor = when {
                        floor < 0 -> Color.Red
                        floor > 0 -> Color.Green
                        else -> Color.LightGray
                    }
                )
            )
            viz.set(9, 1, *tileText((index + 1).toString(), borderColor = Color.LightGray))
            onProgress(viz)
            if (floor < 0) return index + 1
        }
        return 0
    }
}