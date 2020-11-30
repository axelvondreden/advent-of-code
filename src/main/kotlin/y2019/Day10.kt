package y2019

import Day
import Point
import Utils
import gcd
import kotlin.math.abs
import kotlin.math.atan2

class Day10 : Day() {

    override val input = Utils.readCharMatrix(2019, 10)

    private var station = Point(0, 0)

    private val asteroids = mutableListOf<Point>()

    init {
        input.forEachIndexed { x, chars ->
            chars.forEachIndexed { y, c ->
                if (c == '#') {
                    asteroids += Point(x, y)
                }
            }
        }
    }

    override fun solve1(): Int {
        var maxAsteroids = 0
        for (asteroid in asteroids) {
            var count = 0
            for (otherAsteroid in asteroids) {
                if (otherAsteroid != asteroid) {
                    val dx = otherAsteroid.x - asteroid.x
                    val dy = otherAsteroid.y - asteroid.y
                    if (dx == 0 && otherAsteroid == input.firstAsteroid(asteroid, 0, dy / abs(dy))) {
                        count++
                    } else if (dy == 0 && otherAsteroid == input.firstAsteroid(asteroid, dx / abs(dx), 0)) {
                        count++
                    } else if (dx != 0 && dy != 0) {
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

    override fun solve2(): Int {
        val distances = mutableListOf<Point>()
        for (asteroid in asteroids) {
            if (asteroid != station) {
                distances += Point(station.x - asteroid.x, station.y - asteroid.y)
            }
        }

        distances.sortBy { atan2(it.y.toFloat(), it.x.toFloat()) }
        val map = distances.reversed().groupBy { atan2(it.y.toFloat(), it.x.toFloat()) }.toMutableMap()

        val angles = map.keys.toList().sorted().toMutableList()
        val start = map.keys.first { it -> map[it]!!.any { it.x == 0 } }
        var index = angles.indexOf(start)
        var counter = 0

        while (map.isNotEmpty()) {
            val newList = map[angles[index]]!!.toMutableList()
            val next = newList.minByOrNull { abs(it.x) + abs(it.y) }!!
            counter++
            if (counter == 200) {
                return (station.x - next.x) * 100 + (station.y - next.y)
            }
            newList.remove(next)
            if (newList.isEmpty()) {
                map.remove(angles[index])
                angles.removeAt(index)
                index--
            } else {
                map[angles[index]] = newList
            }
            index++
            if (index >= angles.size) {
                index = 0
            }
        }
        return 0
    }

    private fun Array<CharArray>.firstAsteroid(start: Point, dx: Int, dy: Int): Point? {
        var x = start.x + dx
        var y = start.y + dy
        while (x in indices && y in get(x).indices) {
            if (get(x)[y] == '#') {
                return Point(x, y)
            }
            x += dx
            y += dy
        }
        return null
    }
}