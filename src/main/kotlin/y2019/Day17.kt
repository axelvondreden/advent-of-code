package y2019

import Day
import utils.Point
import utils.sumByLong
import utils.toLongArray

class Day17 : Day<LongArray>(2019, 17) {

    override suspend fun List<String>.parse() = first().toLongArray()

    override suspend fun solve1(input: LongArray): Long {
        val computer = IntCodeComputer(input)
        val map = mutableMapOf<Point, Char>()
        var x = 0
        var y = 0
        var out = computer.run()
        while (!out.halted) {
            if (out.value == 10L) {
                x = 0
                y++
            } else {
                map[Point(x, y)] = out.value.toInt().toChar()
                x++
            }
            out = computer.run()
        }

        val intersections = map.filter {
            it.value == '#' &&
                    map.getOrDefault(Point(it.key.x - 1, it.key.y), ' ') == '#' &&
                    map.getOrDefault(Point(it.key.x + 1, it.key.y), ' ') == '#' &&
                    map.getOrDefault(Point(it.key.x, it.key.y - 1), ' ') == '#' &&
                    map.getOrDefault(Point(it.key.x, it.key.y + 1), ' ') == '#'
        }.map { it.key }.toSet()
        return intersections.sumByLong { it.x * it.y }
    }

    override suspend fun solve2(input: LongArray): Long {
        val register = input.copyOf().apply { set(0, 2) }
        val a = "L,6,R,12,L,4,L,6".map { it.code.toLong() }.toMutableList().apply { add(10L) }
        val b = "R,6,L,6,R,12".map { it.code.toLong() }.toMutableList().apply { add(10L) }
        val c = "L,6,L,10,L,10,R,6".map { it.code.toLong() }.toMutableList().apply { add(10L) }
        val main = "A,B,B,C,A,B,C,A,B,C".map { it.code.toLong() }.toMutableList().apply { add(10L) }

        val list = mutableListOf<Long>().apply { addAll(main); addAll(a); addAll(b); addAll(c); add('n'.code.toLong()); add(10L) }

        val computer2 = IntCodeComputer(register).withInputs(list.toLongArray())
        var out = computer2.run()
        var lastOut = out.value
        while (!out.halted) {
            lastOut = out.value
            out = computer2.run()
        }
        return lastOut
    }
}