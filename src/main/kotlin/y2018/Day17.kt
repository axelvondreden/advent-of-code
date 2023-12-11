package y2018

import Day
import utils.Point
import utils.findPoints

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
        map[springLocation.x.toInt()][springLocation.y.toInt()] = '|'
        var running = true
        while (running) {
            running = simulate(map)
        }
        return 0
    }

    private fun simulate(map: Array<CharArray>): Boolean {
        var changes = false
        val flowingWaterLocations = map.findPoints('|').filter { it.y < map[0].lastIndex }
        flowingWaterLocations.forEach { loc ->
            // exclude bottom
            if (loc.x > 0) {
                // left
                when (map[loc.x.toInt() - 1][loc.y.toInt()]) {
                    '.' -> {
                        map[loc.x.toInt() - 1][loc.y.toInt()] = '|'
                        changes = true
                    }

                    '#' -> {
                        // check for wall right
                        var walledIn = true
                        var x = loc.x.toInt()
                        while (x <= map.lastIndex) {
                            when (map[x][loc.y.toInt()]) {
                                '.', '~' -> {
                                    walledIn = false
                                    break
                                }

                                ''
                            }
                            x++
                        }
                    }
                }
            }
        }
        return changes
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