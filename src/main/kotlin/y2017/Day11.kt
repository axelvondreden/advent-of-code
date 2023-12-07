package y2017

import Day
import kotlin.math.absoluteValue

class Day11 : Day<List<String>>(2017, 11) {

    override val input = readString().split(",")

    override fun solve1(input: List<String>) = input.fold(Hex(0, 0, 0)) { spot, dir -> spot.move(dir.toHexDir()) }.distance(Hex(0, 0, 0))

    override fun solve2(input: List<String>) = input.fold(listOf(Hex(0, 0, 0))) { path, dir ->
        path + (path.last().move(dir.toHexDir()))
    }.maxOf { it.distance(Hex(0, 0, 0)) }

    private fun String.toHexDir() = when (this) {
        "n" -> HexDir.N
        "ne" -> HexDir.NE
        "se" -> HexDir.SE
        "s" -> HexDir.S
        "sw" -> HexDir.SW
        "nw" -> HexDir.NW
        else -> throw RuntimeException("oh no!")
    }

    private data class Hex(val x: Int, val y: Int, val z: Int) {
        fun move(direction: HexDir) = when (direction) {
            HexDir.N -> Hex(x, y + 1, z - 1)
            HexDir.NE -> Hex(x + 1, y, z - 1)
            HexDir.SE -> Hex(x + 1, y - 1, z)
            HexDir.S -> Hex(x, y - 1, z + 1)
            HexDir.SW -> Hex(x - 1, y, z + 1)
            HexDir.NW -> Hex(x - 1, y + 1, z)
        }

        fun distance(target: Hex) =
            maxOf((x - target.x).absoluteValue, (y - target.y).absoluteValue, (z - target.z).absoluteValue)
    }

    private enum class HexDir {
        N, S, NE, NW, SE, SW
    }
}
