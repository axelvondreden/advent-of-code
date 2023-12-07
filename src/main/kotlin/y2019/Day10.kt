package y2019

import Day
import utils.Point
import utils.gcd
import utils.toCharMatrix
import kotlin.math.abs
import kotlin.math.atan2

class Day10 : Day<Array<CharArray>>(2019, 10) {

    override fun List<String>.parse() = toCharMatrix()

    private var station = Point(0, 0)

    private val asteroids = mutableListOf<Point>()

    override fun solve1(input: Array<CharArray>): Int {
        var maxAsteroids = 0
        station = Point(0, 0)
        asteroids.clear()
        input.forEachIndexed { x, chars ->
            chars.forEachIndexed { y, c ->
                if (c == '#') asteroids += Point(x, y)
            }
        }
        asteroids.forEach { asteroid ->
            var count = 0
            asteroids.forEach { otherAsteroid ->
                if (otherAsteroid != asteroid) {
                    val dx = otherAsteroid.x - asteroid.x
                    val dy = otherAsteroid.y - asteroid.y
                    if (dx == 0L && otherAsteroid == input.firstAsteroid(asteroid, 0, dy / abs(dy))) {
                        count++
                    } else if (dy == 0L && otherAsteroid == input.firstAsteroid(asteroid, dx / abs(dx), 0)) {
                        count++
                    } else if (dx != 0L && dy != 0L) {
                        val gcd = dx.gcd(dy)
                        if (otherAsteroid == input.firstAsteroid(asteroid, dx / gcd, dy / gcd)) {
                            count++
                        }
                    }
                }
            }
            if (count > maxAsteroids) {
                maxAsteroids = count
                station = asteroid
            }
        }
        return maxAsteroids
    }

    override fun solve2(input: Array<CharArray>): Long {
        val distances = mutableListOf<Point>()
        asteroids.filter { it != station }.forEach {
            distances += Point(station.x - it.x, station.y - it.y)
        }

        distances.sortBy { atan2(it.y.toFloat(), it.x.toFloat()) }
        val map = distances.reversed().groupBy { atan2(it.y.toFloat(), it.x.toFloat()) }.toMutableMap()

        val angles = map.keys.toList().sorted().toMutableList()
        val start = map.keys.first { it -> map[it]!!.any { it.x == 0L } }
        var index = angles.indexOf(start)
        var counter = 0

        while (map.isNotEmpty()) {
            val newList = map[angles[index]]!!.toMutableList()
            val next = newList.minByOrNull { abs(it.x) + abs(it.y) }!!
            counter++
            if (counter == 200) return (station.x - next.x) * 100 + (station.y - next.y)
            newList.remove(next)
            if (newList.isEmpty()) {
                map.remove(angles[index])
                angles.removeAt(index)
                index--
            } else {
                map[angles[index]] = newList
            }
            index++
            if (index >= angles.size) index = 0
        }
        return 0
    }

    private fun Array<CharArray>.firstAsteroid(start: Point, dx: Long, dy: Long): Point? {
        var x = start.x + dx
        var y = start.y + dy
        while (x in indices && y in get(x.toInt()).indices) {
            if (get(x.toInt())[y.toInt()] == '#') return Point(x, y)
            x += dx
            y += dy
        }
        return null
    }
}