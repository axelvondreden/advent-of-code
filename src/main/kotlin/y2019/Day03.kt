package y2019

import Day
import kotlin.math.max
import kotlin.math.min

class Day03 : Day<List<String>>(2019, 3) {

    override val input = readStrings()

    private val steps1 = getSteps(input[0].split(","))
    private val steps2 = getSteps(input[1].split(","))

    override fun solve1(input: List<String>): Int {
        val intersections = steps1.filter { steps2.contains(it) }
        val center = Step(0, 0)
        return intersections.minOf { it.distance(center) }
    }

    override fun solve2(input: List<String>) = getShortestIntersection(steps1, steps2)

    private fun getShortestIntersection(steps1: List<Step>, steps2: List<Step>): Int {
        var min = Int.MAX_VALUE
        for ((index1, step) in steps1.withIndex()) {
            val index2 = steps2.indexOf(step)
            if (index2 > 0 && index1 + index2 < min) {
                min = index1 + index2
            }
        }
        return min + 2
    }

    private fun getSteps(lines: List<String>): List<Step> {
        var x = 0
        var y = 0
        val list: MutableList<Step> = ArrayList()
        for (line in lines) {
            var dx = 0
            var dy = 0
            when (line[0]) {
                'U' -> dy--
                'D' -> dy++
                'L' -> dx--
                'R' -> dx++
            }
            repeat(line.substring(1).toInt()) {
                x += dx
                y += dy
                list.add(Step(x, y))
            }
        }
        return list
    }

    data class Step(val x: Int, val y: Int) {

        fun distance(step: Step): Int = (max(x, step.x) - min(x, step.x)) + (max(y, step.y) - min(y, step.y))

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Step

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }
    }
}