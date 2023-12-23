package y2016

import DayViz
import androidx.compose.ui.graphics.Color
import runner.compose.VizCanvas
import utils.Dir
import utils.FloatPoint
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
        val points = mutableListOf(Point(0, 0))
        var dir = Dir.UP
        input.forEachIndexed { index, step ->
            dir = dir.turn(step.turn)
            repeat(step.amount) {
                points += when (dir) {
                    Dir.LEFT -> points.last() left 1
                    Dir.RIGHT -> points.last() right 1
                    Dir.UP -> points.last() up 1
                    Dir.DOWN -> points.last() down 1
                }
                val viz = getMapViz((index + 1).toDouble() / input.size, points).apply {
                    info["Distance"] = points.first().distance(points.last()).toString()
                }
                onProgress(viz)
            }
        }
        return abs(points.last().x) + abs(points.last().y)
    }

    override suspend fun solve2Visualized(input: List<Step>, onProgress: suspend (VizCanvas) -> Unit): Long {
        var point = Point(0, 0)
        var dir = Dir.UP
        val visited = mutableListOf<Point>()
        input.forEachIndexed { index, step ->
            dir = dir.turn(step.turn)
            repeat(step.amount) {
                point = when (dir) {
                    Dir.LEFT -> point left 1
                    Dir.RIGHT -> point right 1
                    Dir.UP -> point up 1
                    Dir.DOWN -> point down 1
                }
                if (visited.isNotEmpty()) {
                    val viz = getMapViz((index + 1).toDouble() / input.size, visited).apply {
                        info["Distance"] = visited.first().distance(visited.last()).toString()
                    }
                    onProgress(viz)
                }
                if (visited.contains(Point(point.x, point.y))) return abs(point.x) + abs(point.y)
                else visited.add(Point(point.x, point.y))
            }
        }
        return 0
    }

    private fun getMapViz(progress: Double, points: List<Point>): VizCanvas {
        val minX = points.minOf { it.x }
        val minY = points.minOf { it.y }
        val maxX = points.maxOf { it.x }.toInt()
        val maxY = points.maxOf { it.y }.toInt()
        val dx = 0 - minX.toInt()
        val dy = 0 - minY.toInt()
        val width = max(4, maxX + dx + 1).toFloat()
        val height = max(4, maxY + dy + 1).toFloat()
        var last = points.first()
        return VizCanvas(progress).apply {
            points.drop(1).forEach { point ->
                line(Color.White, FloatPoint((last.x + dx) / width, (last.y + dy) / height), FloatPoint((point.x + dx) / width, (point.y + dy) / height))
                last = point
            }
        }
    }

    data class Step(val turn: Turn, val amount: Int)
}