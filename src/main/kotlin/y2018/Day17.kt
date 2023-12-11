package y2018

import Day
import utils.Point
import utils.findPoints
import utils.get
import utils.set

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

    override fun solve1(input: List<Vein>): Int {
        val resultScanRange = input.minOf { it.y.first }..input.maxOf { it.y.last }
        val map = input.toMap()
        map[springLocation.x.toInt()][springLocation.y.toInt()] = '|'
        simulate(map)
        return map.findPoints('~', '|').filter { it.y in resultScanRange }.size
    }

    override fun solve2(input: List<Vein>): Any {
        val resultScanRange = input.minOf { it.y.first }..input.maxOf { it.y.last }
        val map = input.toMap()
        map[springLocation.x.toInt()][springLocation.y.toInt()] = '|'

        return map.findPoints('~').filter { it.y in resultScanRange }.size
    }

    private fun simulate(map: Array<CharArray>) {
        var running = true
        val flowingWaterLocations = map.findPoints('|').filter { it.y < map[0].lastIndex }.toMutableSet() // exclude bottom
        while (running) {
            var changes = false
            for (loc in flowingWaterLocations) {
                if (map[loc down 1] == '.') {
                    // flowing down
                    map[loc down 1] = '|'
                    changes = true
                    if (loc.y < map[0].lastIndex - 1) {
                        flowingWaterLocations += loc down 1
                    }
                    break
                } else if (map[loc down 1] != '|') {
                    if (loc.x > 0) {
                        // left
                        when (map[loc left 1]) {
                            '.' -> {
                                map[loc left 1] = '|'
                                flowingWaterLocations += loc left 1
                                changes = true
                                break
                            }

                            '#' -> {
                                // check for wall right
                                var walledIn = true
                                var i = 1
                                while (loc.x + i <= map.lastIndex) {
                                    when (map[loc right i]) {
                                        '.', '~' -> {
                                            walledIn = false
                                            break
                                        }

                                        '#' -> break
                                    }
                                    i++
                                }
                                if (walledIn) {
                                    (0 until i).forEach {
                                        map[loc right it] = '~'
                                    }
                                    changes = true
                                    break
                                }
                            }
                        }
                    }
                    if (loc.x < map.lastIndex) {
                        // right
                        when (map[loc right 1]) {
                            '.' -> {
                                map[loc right 1] = '|'
                                flowingWaterLocations += loc right 1
                                changes = true
                                break
                            }

                            '#' -> {
                                // check for wall left
                                var walledIn = true
                                var i = 1
                                while (loc.x - i >= 0) {
                                    when (map[loc left i]) {
                                        '.', '~' -> {
                                            walledIn = false
                                            break
                                        }

                                        '#' -> break
                                    }
                                    i++
                                }
                                if (walledIn) {
                                    (0 until i).forEach {
                                        map[loc left it] = '~'
                                    }
                                    changes = true
                                    break
                                }
                            }
                        }
                    }
                }
            }
            running = changes
        }
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