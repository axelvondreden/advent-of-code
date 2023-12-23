package y2016

import DayViz
import androidx.compose.ui.graphics.Color
import runner.compose.Tile
import runner.compose.VizCanvas
import utils.Dir
import utils.Point
import utils.Turn
import kotlin.math.abs
import kotlin.math.max

class Day01 : DayViz<List<Day01.Step>, VizCanvas>(2016, 1) {

    override suspend fun List<String>.parse() = first().split(", ").map {
        Step(if (it[0] == 'L') Turn.LEFT else Turn.RIGHT, it.drop(1).toInt())
    }

    override suspend fun solve1(input: List<Step>): Long {
        var point = Point(0, 0)
        var dir = Dir.UP
        input.forEach { step ->
            dir = dir.turn(step.turn)
            point = when (dir) {
                Dir.LEFT -> point left step.amount
                Dir.RIGHT -> point right step.amount
                Dir.UP -> point up step.amount
                Dir.DOWN -> point down step.amount
            }
        }
        return abs(point.x) + abs(point.y)
    }

    override suspend fun solve2(input: List<Step>): Long {
        var point = Point(0, 0)
        var dir = Dir.UP
        val visited = mutableSetOf<Point>()
        input.forEach { step ->
            dir = dir.turn(step.turn)
            repeat(step.amount) {
                point = when (dir) {
                    Dir.LEFT -> point left 1
                    Dir.RIGHT -> point right 1
                    Dir.UP -> point up 1
                    Dir.DOWN -> point down 1
                }
                if (!visited.add(Point(point.x, point.y))) return abs(point.x) + abs(point.y)
            }
        }
        return 0
    }

    override fun initViz1(input: List<Step>) = VizCanvas().apply {
        info["Distance"] = "0"
    }

    override fun initViz2(input: List<Step>) = VizCanvas().apply {
        info["Distance"] = "0"
    }

    override suspend fun solve1Visualized(input: List<Step>, onProgress: suspend (VizCanvas) -> Unit): Long {
        val points = mutableSetOf(Point(0, 0))
        var dir = Dir.UP
        input.forEachIndexed { index, step ->
            dir = dir.turn(step.turn)
            points += when (dir) {
                Dir.LEFT -> points.last() left step.amount
                Dir.RIGHT -> points.last() right step.amount
                Dir.UP -> points.last() up step.amount
                Dir.DOWN -> points.last() down step.amount
            }
            val viz = getMapViz((index + 1).toDouble() / input.size, points.last(), points).apply {
                info["Distance"] = points.first().distance(points.last()).toString()
            }
            onProgress(viz)
        }
        return abs(points.last().x) + abs(points.last().y)
    }

    override suspend fun solve2Visualized(input: List<Step>, onProgress: suspend (VizCanvas) -> Unit): Long {
        var point = Point(0, 0)
        var dir = Dir.UP
        val visited = mutableSetOf<Point>()
        input.forEachIndexed { index, step ->
            dir = dir.turn(step.turn)
            repeat(step.amount) {
                point = when (dir) {
                    Dir.LEFT -> point left 1
                    Dir.RIGHT -> point right 1
                    Dir.UP -> point up 1
                    Dir.DOWN -> point down 1
                }
                val viz = getMapViz((index + 1).toDouble() / input.size, point, visited).apply {
                    info["Distance"] = visited.first().distance(visited.last()).toString()
                }
                onProgress(viz)
                if (!visited.add(Point(point.x, point.y))) return abs(point.x) + abs(point.y)
            }
        }
        return 0
    }

    private fun getMapViz(progress: Double, location: Point, points: Set<Point>): VizCanvas {
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
        return VizCanvas(progress).apply {
            //grid(0, 0, map)
        }
    }


    data class Step(val turn: Turn, val amount: Int)
}