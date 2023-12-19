package y2016

import Day
import utils.Point
import utils.md5


class Day17 : Day<String>(2016, 17) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String) = mutableSetOf<String>().also {
        testWalk(input, Point(0, 0), "", it)
    }.minByOrNull { it.length }!!

    override suspend fun solve2(input: String) = mutableSetOf<String>().also {
        testWalk(input, Point(0, 0), "", it)
    }.maxByOrNull { it.length }!!.length

    private fun testWalk(input: String, current: Point, path: String, finishes: MutableSet<String>) {
        if (current == Point(3, 3)) {
            finishes.add(path)
            return
        }
        val hash = (input + path).md5().substring(0, 4)
        if (hash[0].isOpen() && current.y > 0) testWalk(input, current + Point(0, -1), path + "U", finishes)
        if (hash[1].isOpen() && current.y < 3) testWalk(input, current + Point(0, 1), path + "D", finishes)
        if (hash[2].isOpen() && current.x > 0) testWalk(input, current + Point(-1, 0), path + "L", finishes)
        if (hash[3].isOpen() && current.x < 3) testWalk(input, current + Point(1, 0), path + "R", finishes)
    }

    private fun Char.isOpen() = this in listOf('b', 'c', 'd', 'e', 'f')
}