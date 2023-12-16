package y2015

import Day
import androidx.compose.ui.graphics.Color
import runner.Tile
import runner.Viz
import runner.tileText

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

    override fun initViz(input: String) = Viz().apply {
        set(0, 0, *tileText("Level:"))
        set(6, 0, *tileText("0", borderColor = Color.LightGray))
        set(0, 5, Tile(borderColor = Color.Yellow))
        set(1, 5, *tileText(input.take(29)))
    }

    override suspend fun solve1Visualized(input: String, onProgress: suspend (Viz) -> Unit): Int {
        var lvl = 0
        for ((index, c) in input.withIndex()) {
            val viz = Viz((index + 1).toDouble() / input.length).apply {
                set(0, 0, *tileText("Level:"))
                set(1, 5, *tileText(input.substring(index + 1).take(29)))
            }

            if (c == '(') {
                lvl++
                viz.set(0, 5, Tile(c, borderColor = Color.Green))
            } else {
                lvl--
                viz.set(0, 5, Tile(c, borderColor = Color.Red))
            }
            viz.set(6, 0, *tileText(lvl.toString(), borderColor = Color.LightGray))

            onProgress(viz)
        }
        return lvl
    }
}