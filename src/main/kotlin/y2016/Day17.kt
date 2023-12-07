package y2016

import Day
import utils.Point
import utils.md5


class Day17 : Day<List<String>>(2016, 17) {

    override val input = readString()

    private val pathList = mutableSetOf<String>().also { testWalk(Point(0, 0), "", it) }

    override fun solve1(input: List<String>) = pathList.minByOrNull { it.length }!!

    override fun solve2(input: List<String>) = pathList.maxByOrNull { it.length }!!.length

    private fun testWalk(current: Point, path: String, finishes: MutableSet<String>) {
        if (current == Point(3, 3)) {
            finishes.add(path)
            return
        }
        val hash = (input + path).md5().substring(0, 4)
        if (hash[0].isOpen() && current.y > 0) testWalk(current + Point(0, -1), path + "U", finishes)
        if (hash[1].isOpen() && current.y < 3) testWalk(current + Point(0, 1), path + "D", finishes)
        if (hash[2].isOpen() && current.x > 0) testWalk(current + Point(-1, 0), path + "L", finishes)
        if (hash[3].isOpen() && current.x < 3) testWalk(current + Point(1, 0), path + "R", finishes)
    }

    private fun Char.isOpen() = this in listOf('b', 'c', 'd', 'e', 'f')
}