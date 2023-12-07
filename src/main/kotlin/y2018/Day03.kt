package y2018

import Day
import utils.Point

class Day03 : Day<Set<Day03.Claim>>(2018, 3) {

    override fun List<String>.parse() = parseClaims()

    override fun solve1(input: Set<Claim>) = input.flatMap { it.getPoints() }.groupBy { it }.values.count { it.size > 1 }

    override fun solve2(input: Set<Claim>) = input.first { claim ->
        (claim.pos.x until claim.pos.x + claim.w).all { x ->
            (claim.pos.y until claim.pos.y + claim.h).all { y ->
                input.count { it.containsPoint(Point(x, y)) } == 1
            }
        }
    }.id

    private fun List<String>.parseClaims() = map { line ->
        val (id, coords) = line.split(" @ ")
        val (x, y) = coords.split(": ")[0].split(",").map { it.toInt() }
        val (w, h) = coords.split(": ")[1].split("x").map { it.toInt() }
        Claim(id.drop(1).toInt(), Point(x, y), w, h)
    }.toSet()

    data class Claim(val id: Int, val pos: Point, val w: Int, val h: Int) {
        fun containsPoint(point: Point) = point.x in (this.pos.x until this.pos.x + w) && point.y in (this.pos.y until this.pos.y + h)

        fun getPoints(): Set<Point> {
            val list = mutableSetOf<Point>()
            (this.pos.x until this.pos.x + w).forEach { x ->
                (this.pos.y until this.pos.y + h).forEach { y ->
                    list.add(Point(x, y))
                }
            }
            return list
        }
    }
}