package y2018

import Day
import utils.Point
import utils.print

class Day17 : Day<List<Day17.Vein>>(2018, 17) {

    override fun List<String>.parse(): List<Vein> = map {
        val s = it.split(", ")
        val s1 = s[0].split("=")
        val s2 = s[1].split("=")
        val s3 = s2[1].split("..")
        if (s1[0] == "x") {
            Vein(s1[1].toInt()..s1[1].toInt(), s3[0].toInt()..s3[1].toInt())
        } else {
            Vein(s3[0].toInt()..s3[1].toInt(), s1[1].toInt()..s1[1].toInt())
        }
    }

    private val springLocation = Point(500, 0)

    override fun solve1(input: List<Vein>): Any {
        val resultScanRange = input.minOf { it.y.first }..input.maxOf { it.y.last }
        val map = input.toMap()
        return 0
    }

    override fun solve2(input: List<Vein>): Any {
        return 0
    }

    data class Vein(val x: IntRange, val y: IntRange) {
        fun contains(x: Int, y: Int) = x in this.x && y in this.y
    }

    private fun List<Vein>.toMap(): Array<CharArray> {
        val maxY = maxOf { it.y.last }
        val maxX = maxOf { it.x.last }
        return Array(maxX + 1) { x ->
            CharArray(maxY + 1) { y ->
                if (any { it.contains(x, y) }) {
                    '#'
                } else '.'
            }
        }
    }
}