package y2023

import Day
import utils.Point3D
import kotlin.math.max
import kotlin.math.min

class Day22 : Day<List<Day22.Brick>>(2023, 22) {

    override suspend fun List<String>.parse() = map { line ->
        val split = line.split("~")
        val p1 = Point3D(split[0])
        val p2 = Point3D(split[1])
        val xRange = min(p1.x, p2.x)..max(p1.x, p2.x)
        val yRange = min(p1.y, p2.y)..max(p1.y, p2.y)
        val zRange = min(p1.z, p2.z)..max(p1.z, p2.z)
        val points = mutableSetOf<Point3D>()
        xRange.forEach { x ->
            yRange.forEach { y ->
                zRange.forEach { z ->
                    points += Point3D(x, y, z)
                }
            }
        }
        Brick(points)
    }

    override suspend fun solve1(input: List<Brick>): Int {
        val settled = input.settle()
        println("Settled")
        return settled.withIndex().count { (index, brick) ->
            println("Testing $index / ${settled.size}")
            val withoutBrick = settled.filterNot { it == brick }
            !withoutBrick.isNotSame(withoutBrick.settleOnce())
        }
    }

    override suspend fun solve2(input: List<Brick>): Int {
        val settled = input.settle()
        println("Settled")
        return settled.withIndex().sumOf { (index, brick) ->
            println("Testing $index / ${settled.size}")
            val withoutBrick = settled.filterNot { it == brick }
            val settledWithoutBrick = withoutBrick.settleOnce()
            settledWithoutBrick.count { it !in withoutBrick }
        }
    }

    private fun List<Brick>.settle(): List<Brick> {
        var last = this
        var next = last.settleOnce()
        while (next.isNotSame(last)) {
            last = next
            next = last.settleOnce()
        }
        return next
    }

    private fun List<Brick>.settleOnce(): List<Brick> {
        val new = mutableListOf<Brick>()
        sortedBy { it.lowestZ }.forEach { brick ->
            new += if (brick.lowestZ == 1L) {
                brick
            } else {
                var newBrick = brick
                var movedDown = brick.moveDown()
                while (movedDown.lowestZ >= 1 && new.none { it.intersects(movedDown) }) {
                    newBrick = movedDown
                    movedDown = movedDown.moveDown()
                }
                newBrick
            }
        }
        return new
    }

    data class Brick(val points: Set<Point3D>) {

        val lowestZ by lazy { points.minOf { it.z } }

        fun moveDown() = Brick(points.map { it.copy(z = it.z - 1) }.toSet())

        fun intersects(brick: Brick) = points.any { it in brick.points }
    }

    private fun List<Brick>.isNotSame(list: List<Brick>) = size != list.size || any { it !in list } || list.any { it !in this }
}