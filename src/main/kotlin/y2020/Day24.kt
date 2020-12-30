package y2020

import Day
import kotlin.math.absoluteValue


class Day24 : Day(2020, 24) {

    override val input = parseInput(readStrings())

    private val tiles = mutableMapOf<Pair<Int, Int>, Boolean>()

    override fun solve1(): Int {
        input.forEach { path ->
            val tile = path.fold(0 to 0) { current, dir -> current.move(dir) }
            tiles[tile] = if (tile !in tiles) false else !(tiles[tile]!!)
        }
        return tiles.values.count { !it }
    }

    override fun solve2(): Int {
        var tiles = this.tiles.toMap()
        repeat(100) {
            tiles = tiles.flip()
        }
        return tiles.values.count { !it }
    }

    private fun Pair<Int, Int>.move(direction: HexDir) = when (direction) {
        HexDir.E -> if (second.absoluteValue % 2 == 1) first + 1 to second else first + 1 to second
        HexDir.NE -> if (second.absoluteValue % 2 == 1) first + 1 to second - 1 else first to second - 1
        HexDir.SE -> if (second.absoluteValue % 2 == 1) first + 1 to second + 1 else first to second + 1
        HexDir.W -> if (second.absoluteValue % 2 == 1) first - 1 to second else first - 1 to second
        HexDir.SW -> if (second.absoluteValue % 2 == 1) first to second + 1 else first - 1 to second + 1
        HexDir.NW -> if (second.absoluteValue % 2 == 1) first to second - 1 else first - 1 to second - 1
    }

    private fun Map<Pair<Int, Int>, Boolean>.flip(): Map<Pair<Int, Int>, Boolean> {
        val new = mutableMapOf<Pair<Int, Int>, Boolean>()
        val blacks = filter { !it.value }
        blacks.forEach { (tile, white) ->
            if (tile !in new) {
                new[tile] = this.getNextSide(tile, white)
            }
            val neighbours = getNeighbours(tile)
            neighbours.forEach { (nTile, nWhite) ->
                if (nTile !in new) {
                    new[nTile] = this.getNextSide(nTile, nWhite)
                }
            }
        }
        return new
    }

    private fun Map<Pair<Int, Int>, Boolean>.getNextSide(tile: Pair<Int, Int>, white: Boolean): Boolean {
        val count = countBlackNeighbours(tile)
        return when {
            white && count == 2 -> false
            !white && (count == 0 || count > 2) -> true
            else -> white
        }
    }

    private fun Map<Pair<Int, Int>, Boolean>.getNeighbours(tile: Pair<Int, Int>) = listOf(
        tile.move(HexDir.E),
        tile.move(HexDir.W),
        tile.move(HexDir.NE),
        tile.move(HexDir.SE),
        tile.move(HexDir.NW),
        tile.move(HexDir.SW)
    ).map { it to getOrDefault(it, true) }.toMap()

    private fun Map<Pair<Int, Int>, Boolean>.countBlackNeighbours(tile: Pair<Int, Int>) = getNeighbours(tile).values.count { !it }

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

    enum class HexDir {
        E, SE, SW, W, NW, NE
    }
}