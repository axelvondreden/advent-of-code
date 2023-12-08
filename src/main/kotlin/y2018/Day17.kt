package y2018

import Day
import utils.Point

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
        return 0
    }

    override fun solve2(input: List<Vein>): Any {
        return 0
    }

    data class Vein(val x: IntRange, val y: IntRange)
}