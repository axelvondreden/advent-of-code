package y2017

import Day
import utils.Point3D

class Day20 : Day<Set<Day20.Particle>>(2017, 20) {

    override suspend fun List<String>.parse() = parseParticles()

    override suspend fun solve1(input: Set<Particle>) = input.minByOrNull { it.acc.abs() }!!.id

    override suspend fun solve2(input: Set<Particle>): Int {
        var copy = input
        var lastCollision = 0
        while (lastCollision < 100) {
            copy.forEach { it.update() }
            val dupes = copy.filter { part -> copy.any { it.id != part.id && it.pos == part.pos } }
            if (dupes.isNotEmpty()) {
                lastCollision = 0
                copy = copy.minus(dupes.toSet())
            } else {
                lastCollision++
            }
        }
        return copy.size
    }

    private fun List<String>.parseParticles() = mapIndexed { index, s ->
        val split = s.split(" ").map { it.drop(3).dropLast(1).replace(">", "") }
        Particle(index, Point3D(split[0]), Point3D(split[1]), Point3D(split[2]))
    }.toSet()

    data class Particle(val id: Int, var pos: Point3D, var vel: Point3D, val acc: Point3D) {
        fun update() {
            vel += acc
            pos += vel
        }
    }
}
