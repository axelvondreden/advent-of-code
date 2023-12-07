package y2021

import Day
import utils.Line
import utils.Point
import kotlin.math.max

class Day05 : Day<List<String>>(2021, 5) {

    override fun List<String>.parse() = this.map { with(it.split(" -> ")) { Line(Point(this[0]), Point(this[1])) } }

    override fun solve1(input: List<String>) = input.filter { it.isStraight() }.calculateDangerZones()

    override fun solve2(input: List<String>) = input.calculateDangerZones()

    private fun List<Line>.calculateDangerZones(): Int {
        val max = maxOf { line -> line.getPoints().maxOf { max(it.x, it.y) } }.toInt()
        val field = Array(max + 1) { IntArray(max + 1) }
        flatMap { it.getPoints() }.forEach {
            field[it.x.toInt()][it.y.toInt()]++
        }
        return field.sumOf { row -> row.count { it > 1 } }
    }
}