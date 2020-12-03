package y2016

import Day
import Point
import Utils
import md5


class Day17 : Day() {

    override val input = Utils.readString(2016, 17)

    private val pathList = mutableSetOf<String>()

    init {
        testWalk(Point(0, 0), "", pathList)
    }

    override fun solve1() = pathList.minByOrNull { it.length }!!

    override fun solve2() = pathList.maxByOrNull { it.length }!!.length

    private fun testWalk(current: Point, path: String, finishes: MutableSet<String>) {
        if (current == Point(3, 3)) {
            finishes.add(path)
            return
        }
        val hash = (input + path).md5().substring(0, 4)
        if (hash[0].isOpen() && current.y > 0) testWalk(current.plus(0, -1), path + "U", finishes)
        if (hash[1].isOpen() && current.y < 3) testWalk(current.plus(0, 1), path + "D", finishes)
        if (hash[2].isOpen() && current.x > 0) testWalk(current.plus(-1, 0), path + "L", finishes)
        if (hash[3].isOpen() && current.x < 3) testWalk(current.plus(1, 0), path + "R", finishes)
    }

    private fun Char.isOpen() = this in listOf('b', 'c', 'd', 'e', 'f')
}