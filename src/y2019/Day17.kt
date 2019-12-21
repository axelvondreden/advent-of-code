package y2019

import Day
import Point
import Utils

class Day17 : Day() {

    override val input = Utils.readLongArray(2019, 17)

    override fun solve1(): Int {
        val computer = IntCodeComputer(input)
        val map = mutableMapOf<Point, Char>()
        var x = 0
        var y = 0
        var out = computer.run(false)
        while (out != -999L) {
            when (out) {
                10L -> {
                    x = 0
                    y++
                }
                else -> {
                    map[Point(x, y)] = out.toChar()
                    x++
                }
            }
            out = computer.run(false)
        }

        val intersections = map.filter {
            it.value == '#' &&
                    map.getOrDefault(Point(it.key.x - 1, it.key.y), ' ') == '#' &&
                    map.getOrDefault(Point(it.key.x + 1, it.key.y), ' ') == '#' &&
                    map.getOrDefault(Point(it.key.x, it.key.y - 1), ' ') == '#' &&
                    map.getOrDefault(Point(it.key.x, it.key.y + 1), ' ') == '#'
        }.map { it.key }.toSet()
        return intersections.sumBy { it.x * it.y }
    }

    override fun solve2(): Long {
        val register = input.copyOf().apply { set(0, 2) }
        val a = "L,6,R,12,L,4,L,6".map { it.toLong() }.toMutableList().apply { add(10L) }
        val b = "R,6,L,6,R,12".map { it.toLong() }.toMutableList().apply { add(10L) }
        val c = "L,6,L,10,L,10,R,6".map { it.toLong() }.toMutableList().apply { add(10L) }
        val main = "A,B,B,C,A,B,C,A,B,C".map { it.toLong() }.toMutableList().apply { add(10L) }

        val list =
            mutableListOf<Long>().apply { addAll(main); addAll(a); addAll(b); addAll(c); add('n'.toLong()); add(10L) }

        val computer2 = IntCodeComputer(register).withInputs(list.toLongArray())
        var out = computer2.run(false)
        var lastOut = out
        while (out != -999L) {
            lastOut = out
            out = computer2.run(false)
        }
        return lastOut
    }

    private fun printMap(map: Map<Point, Char>) {
        val maxX = map.keys.map { it.x }.max()!!
        val maxY = map.keys.map { it.y }.max()!!
        for (yy in 0..maxY) {
            for (xx in 0..maxX) {
                print(map[Point(xx, yy)])
            }
            println()
        }
    }
}