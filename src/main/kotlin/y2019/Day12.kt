package y2019

import Day
import kotlin.math.absoluteValue

class Day12 : Day<List<List<String>>>(2019, 12) {

    override suspend fun List<String>.parse() = map { it.drop(1).dropLast(1).split(", ") }

    override suspend fun solve1(input: List<List<String>>): Int {
        val planets = input.map {
            Planet(
                it[0].split("=")[1].toInt(),
                it[1].split("=")[1].toInt(),
                it[2].split("=")[1].toInt()
            )
        }
        repeat(1000) {
            step(planets)
        }
        return planets.sumOf(::getTotalEnergy)
    }

    override suspend fun solve2(input: List<List<String>>): Long {
        val planets = input.map {
            Planet(
                it[0].split("=")[1].toInt(),
                it[1].split("=")[1].toInt(),
                it[2].split("=")[1].toInt()
            )
        }
        val start = arrayOf(planets[0].copy(), planets[1].copy(), planets[2].copy(), planets[3].copy())

        var i = 0L
        do {
            step(planets)
            i++
        } while (!planets.toTypedArray().contentEquals(start))
        return i
    }

    private fun step(planets: List<Planet>) {
        planets.indices.forEach { i ->
            (i + 1 until planets.size).forEach { j ->
                applyGravity(planets[i], planets[j])
            }
        }
        planets.forEach(::applyVelocity)
    }

    private fun getTotalEnergy(planet: Planet) =
        (planet.x.absoluteValue + planet.y.absoluteValue + planet.z.absoluteValue) * (planet.vx.absoluteValue + planet.vy.absoluteValue + planet.vz.absoluteValue)

    private fun applyVelocity(planet: Planet) {
        planet.x += planet.vx
        planet.y += planet.vy
        planet.z += planet.vz
    }

    private fun applyGravity(p1: Planet, p2: Planet) {
        if (p1.x < p2.x) {
            p1.vx++
            p2.vx--
        } else if (p1.x > p2.x) {
            p1.vx--
            p2.vx++
        }
        if (p1.y < p2.y) {
            p1.vy++
            p2.vy--
        } else if (p1.y > p2.y) {
            p1.vy--
            p2.vy++
        }
        if (p1.z < p2.z) {
            p1.vz++
            p2.vz--
        } else if (p1.z > p2.z) {
            p1.vz--
            p2.vz++
        }
    }

    private data class Planet(var x: Int, var y: Int, var z: Int, var vx: Int = 0, var vy: Int = 0, var vz: Int = 0)
}