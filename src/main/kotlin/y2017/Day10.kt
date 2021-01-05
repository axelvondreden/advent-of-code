package y2017

import Day

class Day10 : Day(2017, 10) {

    override val input = readString()

    override fun solve1(): Int {
        var list = (0..255).toList()
        var position = 0
        var skipSize = 0
        input.split(",").map(String::toInt).forEach {
            val rotated = list.rotate(-position)
            list = rotated.subList(0, it).reversed().plus(rotated.subList(it, rotated.size)).rotate(position)
            position += it + skipSize
            position %= list.size
            skipSize++
        }
        return list[0] * list[1]
    }

    override fun solve2(): String {
        var list = (0..255).toList()
        var position = 0
        var skipSize = 0
        repeat(64) {
            input.map { it.toInt() }.plus(listOf(17, 31, 73, 47, 23)).forEach {
                val rotated = list.rotate(-position)
                list = rotated.subList(0, it).reversed().plus(rotated.subList(it, rotated.size)).rotate(position)
                position += it + skipSize
                position %= list.size
                skipSize++
            }
        }
        list = list.denseHash()
        return list.joinToString("") { it.toString(16) }
    }

    private fun List<Int>.denseHash(): List<Int> {
        val dense = mutableListOf<Int>()
        chunked(16).forEach { chunk ->
            var res = chunk[0]
            chunk.drop(1).forEach {
                res = res xor it
            }
            dense.add(res)
        }
        return dense
    }

    private fun List<Int>.rotate(rotation: Int) = if (rotation > 0) {
        subList(size - rotation, size).plus(subList(0, size - rotation))
    } else {
        subList(-rotation, size).plus(subList(0, -rotation))
    }
}
