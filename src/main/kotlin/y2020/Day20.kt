package y2020

import Day
import utils.Point
import utils.sqrt


class Day20 : Day(2020, 20) {

    override val input = readStrings().joinToString("|").toTiles()
    private val img = createImage()

    override fun solve1() =
        img.first().first().id * img.first().last().id * img.last().first().id * img.last().last().id

    override fun solve2(): Int {
        val monsterPattern = listOf(
            Point(0, 18), Point(1, 0), Point(1, 5), Point(1, 6), Point(1, 11),
            Point(1, 12), Point(1, 17), Point(1, 18), Point(1, 19), Point(2, 1),
            Point(2, 4), Point(2, 7), Point(2, 10), Point(2, 13), Point(2, 16)
        )

        return imageToSingleTile().orientations()
            .first { it.maskIfFound(monsterPattern) }.data.sumBy { row -> row.count { char -> char == '#' } }
    }

    private fun imageToSingleTile(): Tile {
        val rows = input.first().data.size
        val data = img.flatMap { row ->
            (1 until rows - 1).map { y -> row.joinToString("") { it.insetRow(y) }.toCharArray() }
        }.toTypedArray()
        return Tile(0, data)
    }

    private fun createImage(): List<List<Tile>> {
        val width = input.count().sqrt()
        var mostRecentTile: Tile = findTopCorner()
        var mostRecentRowHeader: Tile = mostRecentTile
        return (0 until width).map { row ->
            (0 until width).map { col ->
                when {
                    row == 0 && col == 0 -> mostRecentTile
                    col == 0 -> {
                        mostRecentRowHeader =
                            mostRecentRowHeader.findAndOrientNeighbor(Orientation.SOUTH, Orientation.NORTH, input)
                        mostRecentTile = mostRecentRowHeader
                        mostRecentRowHeader
                    }
                    else -> {
                        mostRecentTile = mostRecentTile.findAndOrientNeighbor(Orientation.EAST, Orientation.WEST, input)
                        mostRecentTile
                    }
                }
            }
        }
    }

    private fun findTopCorner() = input.first { tile -> tile.sharedSideCount(input) == 2 }
        .orientations()
        .first {
            it.isSideShared(Orientation.SOUTH, input) && it.isSideShared(Orientation.EAST, input)
        }

    private fun String.toTiles() = split("||").map { it.split("|") }.map { tileText ->
        val id = tileText.first().substringAfter(" ").substringBefore(":").toLong()
        val body = tileText.drop(1).map { it.toCharArray() }.toTypedArray()
        Tile(id, body)
    }

    enum class Orientation {
        NORTH, EAST, SOUTH, WEST
    }

    class Tile(val id: Long, var data: Array<CharArray>) {

        private val sides = Orientation.values().map { sideFacing(it) }.toSet()
        private val sidesReversed = sides.map { it.reversed() }.toSet()

        fun sharedSideCount(tiles: List<Tile>) = sides.sumOf { side ->
            tiles.filterNot { it.id == id }.count { tile -> tile.hasSide(side) }
        }

        fun isSideShared(dir: Orientation, tiles: List<Tile>) =
            tiles.filterNot { it.id == id }.any { tile -> tile.hasSide(sideFacing(dir)) }

        fun findAndOrientNeighbor(mySide: Orientation, theirSide: Orientation, tiles: List<Tile>): Tile {
            val mySideValue = sideFacing(mySide)
            return tiles.filterNot { it.id == id }.first { it.hasSide(mySideValue) }
                .also { it.orientToSide(mySideValue, theirSide) }
        }

        fun insetRow(row: Int): String = data[row].drop(1).dropLast(1).joinToString("")

        fun maskIfFound(mask: List<Point>): Boolean {
            var found = false
            val maxWidth = mask.maxByOrNull { it.y }!!.y
            val maxHeight = mask.maxByOrNull { it.x }!!.x
            (0..(data.size - maxHeight)).forEach { x ->
                (0..(data.size - maxWidth)).forEach { y ->
                    val lookingAt = Point(x, y)
                    val actualSpots = mask.map { it + lookingAt }
                    if (actualSpots.all { data[it.x][it.y] == '#' }) {
                        found = true
                        actualSpots.forEach { data[it.x][it.y] = '0' }
                    }
                }
            }
            return found
        }

        fun orientations() = sequence {
            repeat(2) {
                repeat(4) {
                    yield(this@Tile.rotateClockwise())
                }
                this@Tile.flipped()
            }
        }

        private fun hasSide(side: String) = side in sides || side in sidesReversed

        private fun flipped(): Tile {
            data = data.map { it.reversed().toCharArray() }.toTypedArray()
            return this
        }

        private fun rotateClockwise(): Tile {
            data = data.mapIndexed { x, row ->
                row.mapIndexed { y, _ -> data[y][x] }.reversed().toCharArray()
            }.toTypedArray()
            return this
        }

        private fun sideFacing(dir: Orientation) = when (dir) {
            Orientation.NORTH -> data.first().joinToString("")
            Orientation.SOUTH -> data.last().joinToString("")
            Orientation.WEST -> data.map { row -> row.first() }.joinToString("")
            Orientation.EAST -> data.map { row -> row.last() }.joinToString("")
        }

        private fun orientToSide(side: String, direction: Orientation) =
            orientations().first { it.sideFacing(direction) == side }
    }
}