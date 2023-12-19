package y2019

import Day
import utils.Point
import utils.toLongArray

class Day11 : Day<LongArray>(2019, 11) {

    override suspend fun List<String>.parse() = first().toLongArray()

    override suspend fun solve1(input: LongArray): Int {
        val robot = HullRobot(input.copyOf())
        val panels = mutableMapOf<Point, Long>()
        try {
            while (true) {
                val output = robot.run(panels.getOrDefault(robot.getPosition(), 0L))
                panels[Point(output.first, output.second)] = output.third
            }
        } catch (e: Error) {
            return panels.size
        }
    }

    override suspend fun solve2(input: LongArray) {
        var ret = "\n"
        val robot = HullRobot(input)
        val panels = mutableMapOf(Point(0, 0) to 1L)
        try {
            while (true) {
                val output = robot.run(panels.getOrDefault(robot.getPosition(), 0L))
                panels[Point(output.first, output.second)] = output.third
            }
        } catch (e: Error) {
            val minX = panels.keys.minOf { it.x }
            val maxX = panels.keys.maxOf { it.x }
            val minY = panels.keys.minOf { it.y }
            val maxY = panels.keys.maxOf { it.y }
            (minY..maxY).forEach { y ->
                (minX..maxX).forEach { x ->
                    ret += if (panels.getOrDefault(Point(x, y), 0L) == 1L) '#' else ' '
                }
                ret += '\n'
            }
        }
    }

    private class HullRobot(register: LongArray) {

        private var x = 0
        private var y = 0
        private var dir = 1

        private val comp = IntCodeComputer(register)

        fun run(input: Long): Triple<Int, Int, Long> {
            val color = comp.addInput(input).run().value
            val output = Triple(x, y, color)
            if (color == -1L) throw Error("finished")
            when (comp.run().value) {
                0L -> if (dir > 0) dir-- else dir = 3
                1L -> if (dir < 3) dir++ else dir = 0
            }
            when (dir) {
                0 -> x--
                1 -> y--
                2 -> x++
                3 -> y++
            }
            return output
        }

        fun getPosition() = Point(x, y)
    }
}