package y2015

import Day
import androidx.compose.ui.graphics.Color
import runner.compose.Tile
import runner.compose.Viz
import utils.Point
import kotlin.math.max

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

    override fun initViz1(input: List<Char>) = Viz(width = vizWidth).apply {
        info["Houses"] = "0"
    }

    override fun initViz2(input: List<Char>) = Viz(width = vizWidth).apply {
        info["Houses"] = "0"
    }

    override suspend fun solve1Visualized(input: List<Char>, onProgress: suspend (Viz) -> Unit): Int {
        with(mutableMapOf(Point(0, 0) to 1)) {
            runSteps(input, onProgress)
            return size
        }
    }

    override suspend fun solve2Visualized(input: List<Char>, onProgress: suspend (Viz) -> Unit): Int {
        with(mutableMapOf(Point(0, 0) to 1)) {
            runSteps(input.filterIndexed { index, _ -> index % 2 == 0 }, onProgress)
            runSteps(input.filterIndexed { index, _ -> index % 2 == 1 }, onProgress)
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
                val viz = getMapViz((index + 1).toDouble() / steps.size.toDouble(), point, keys).apply {
                    info["Houses"] = size.toString()
                }
                onProgress(viz)
            }
        }
    }

    private fun getMapViz(progress: Double, location: Point, points: Set<Point>): Viz {
        val minX = points.minOf { it.x }
        val minY = points.minOf { it.y }
        val maxX = points.maxOf { it.x }.toInt()
        val maxY = points.maxOf { it.y }.toInt()
        val dx = 0 - minX.toInt()
        val dy = 0 - minY.toInt()
        val width = max(vizWidth, maxX + dx + 1)
        val height = max(vizHeight, maxY + dy + 1)
        val map = Array(width) { Array(height) { Tile() } }
        points.forEach {
            with(map[it.x.toInt() + dx][it.y.toInt() + dy]) {
                backgroundColor = Color.Gray
            }
        }
        map[location.x.toInt() + dx][location.y.toInt() + dy].borderColor = Color.Yellow
        return Viz(progress, width, height).apply {
            grid(0, 0, map)
        }
    }

    override val vizDelay: Long get() = 100
    override val vizWidth: Int get() = 10
}