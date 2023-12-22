package y2023

import Day
import utils.Point3D
import kotlin.math.max
import kotlin.math.min

class Day22 : Day<List<Day22.Brick>>(2023, 22) {

    override suspend fun List<String>.parse() = mapIndexed { index, line ->
        val split = line.split("~")
        val p1 = Point3D(split[0])
        val p2 = Point3D(split[1])
        val xRange = min(p1.x, p2.x)..max(p1.x, p2.x)
        val yRange = min(p1.y, p2.y)..max(p1.y, p2.y)
        val zRange = min(p1.z, p2.z)..max(p1.z, p2.z)
        Brick(
            index,
            xRange.first.toInt(), xRange.last.toInt(),
            yRange.first.toInt(), yRange.last.toInt(),
            zRange.first.toInt(), zRange.last.toInt()
        )
    }

    override suspend fun solve1(input: List<Brick>): Int {
        val settled = input.settle()
        return settled.count { brick ->
            val withoutBrick = settled.filterNot { it == brick }
            !withoutBrick.isNotSame(withoutBrick.settleOnce())
        }
    }

    override suspend fun solve2(input: List<Brick>): Int {
        val settled = input.settle()
        return settled.sumOf { brick ->
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
        sortedBy { it.z1 }.forEach { brick ->
            new += if (brick.z1 == 1) {
                brick
            } else {
                var newBrick = brick
                var movedDown = brick.moveDown()
                while (movedDown.z1 >= 1 && new.none { it.intersects(movedDown) }) {
                    newBrick = movedDown
                    movedDown = movedDown.moveDown()
                }
                newBrick
            }
        }
        return new
    }

    data class Brick(val nr: Int, val x1: Int, val x2: Int, val y1: Int, val y2: Int, val z1: Int, val z2: Int) {

        fun moveDown() = copy(z1 = z1 - 1, z2 = z2 - 1)

        fun intersects(brick: Brick) =
            x1 <= brick.x2 && x2 >= brick.x1
                && y1 <= brick.y2 && y2 >= brick.y1
                && z1 <= brick.z2 && z2 >= brick.z1
    }

    private fun List<Brick>.isNotSame(list: List<Brick>) =
        size != list.size || any { it !in list } || list.any { it !in this }
}