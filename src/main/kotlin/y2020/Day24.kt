package y2020

import Day
import utils.IO
import java.lang.RuntimeException
import kotlin.math.absoluteValue


class Day24 : Day() {

    override val input = parseInput(IO.readStrings(2020, 24))

    override fun solve1(): Int {
        val tiles = mutableListOf(Hex(0, 0, 0))
        input.forEach { path ->
            var tile = Hex(0, 0, 0)
            path.forEach {
                var new = tile.move(it)
                if (new !in tiles) {
                    tiles.add(new)
                }
                new = tiles[tiles.indexOf(new)]
                new.white = !new.white
                tile = new
            }
        }
        return tiles.count { !it.white }
    }

    override fun solve2() = 0

    private fun parseInput(input: List<String>): List<List<HexDir>> {
        val paths = mutableListOf<List<HexDir>>()
        input.forEach {
            var index = 0
            val path = mutableListOf<HexDir>()
            while (index < it.length) {
                val next = it.substring(index, if (it[index] in listOf('e', 'w')) index + 1 else index + 2)
                path += when (next) {
                    "e" -> HexDir.E
                    "w" -> HexDir.W
                    "ne" -> HexDir.NE
                    "se" -> HexDir.SE
                    "nw" -> HexDir.NW
                    "sw" -> HexDir.SW
                    else -> throw RuntimeException("oh no!")
                }
                index += next.length
            }
            paths += path
        }
        return paths
    }

    data class Hex(val x: Int, val y: Int, val z: Int, var white: Boolean = true) {
        fun move(direction: HexDir) = when (direction) {
            HexDir.E -> Hex(x + 1, y + 1, z - 1)
            HexDir.NE -> Hex(x + 1, y, z - 1)
            HexDir.SE -> Hex(x + 1, y - 1, z)
            HexDir.W -> Hex(x, y - 1, z + 1)
            HexDir.SW -> Hex(x - 1, y, z + 1)
            HexDir.NW -> Hex(x - 1, y + 1, z)
        }

        fun distance(target: Hex) =
            maxOf((x - target.x).absoluteValue, (y - target.y).absoluteValue, (z - target.z).absoluteValue)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Hex
            if (x != other.x) return false
            if (y != other.y) return false
            if (z != other.z) return false
            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            result = 31 * result + z
            return result
        }
    }

    enum class HexDir {
        E, SE, SW, W, NW, NE
    }
}