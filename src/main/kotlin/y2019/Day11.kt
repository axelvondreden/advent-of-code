package y2019

import Day
import utils.Point

class Day11 : Day(2019, 11) {

    override val input = readLongArray()

    override fun solve1(): Int {
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

    override fun solve2() {
        var ret = "\n"
        val robot = HullRobot(input)
        val panels = mutableMapOf(Point(0, 0) to 1L)
        try {
            while (true) {
                val output = robot.run(panels.getOrDefault(robot.getPosition(), 0L))
                panels[Point(output.first, output.second)] = output.third
            }
        } catch (e: Error) {
            val minX = panels.keys.map { it.x }.minOrNull()!!
            val maxX = panels.keys.map { it.x }.maxOrNull()!!
            val minY = panels.keys.map { it.y }.minOrNull()!!
            val maxY = panels.keys.map { it.y }.maxOrNull()!!
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    ret += if (panels.getOrDefault(Point(x, y), 0L) == 1L) '#' else ' '
                }
                ret += '\n'
            }
        }
    }

    class HullRobot(register: LongArray) {

        private var x = 0
        private var y = 0
        private var dir = 1

        private val comp = IntCodeComputer(register)

        fun run(input: Long): Triple<Int, Int, Long> {
            val color = comp.addInput(input).run().value
            val output = Triple(x, y, color)
            if (color == -1L) {
                throw Error("finished")
            }
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