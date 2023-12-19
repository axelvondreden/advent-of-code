package y2015

import Day
import androidx.compose.ui.graphics.Color
import runner.compose.Viz
import utils.Point

class Day03 : Day<List<Char>>(2015, 3) {

    override suspend fun List<String>.parse() = this[0].toCharArray().asList()

    override suspend fun solve1(input: List<Char>): Int {
        with(mutableMapOf(Point(0, 0) to 1)) {
            runSteps(input)
            return size
        }
    }

    override suspend fun solve2(input: List<Char>): Int {
        with(mutableMapOf(Point(0, 0) to 1)) {
            runSteps(input.filterIndexed { index, _ -> index % 2 == 0 })
            runSteps(input.filterIndexed { index, _ -> index % 2 == 1 })
            return size
        }
    }

    override fun initViz1(input: List<Char>) = Viz().apply {
        text(0, 0, "Houses:0", borderColor = Color.White)
        text(0, 1, input.take(width).toString())
        borderColor(0, 1, Color.Yellow)
    }

    override fun initViz2(input: List<Char>) = Viz().apply {
        text(0, 0, "Houses:0", borderColor = Color.White)
        text(0, 1, input.take(width).toString())
        borderColor(0, 1, Color.Yellow)
    }

    override suspend fun solve1Visualized(input: List<Char>, onProgress: suspend (Viz) -> Unit): Int {
        with(mutableMapOf(Point(0, 0) to 1)) {
            runSteps(input, onProgress)
            return size
        }
    }

    private suspend fun MutableMap<Point, Int>.runSteps(steps: List<Char>, onProgress: (suspend (Viz) -> Unit)? = null) {
        var point = Point(0, 0)
        steps.forEachIndexed { index, it ->
            when (it) {
                '<' -> point += Point(-1, 0)
                '>' -> point += Point(1, 0)
                '^' -> point += Point(0, -1)
                'v' -> point += Point(0, 1)
            }
            put(point, getOrDefault(point, 0) + 1)
            if (onProgress != null) {
                onProgress(Viz(progress = (index + 1).toDouble() / steps.size.toDouble()).apply {
                    text(0, 0, "Houses:$size", borderColor = Color.White)
                    text(0, 1, steps.toString().substring(index).take(width))
                    borderColor(0, 1, Color.Yellow)
                })
            }
        }
    }
}