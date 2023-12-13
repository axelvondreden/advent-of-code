package y2023

import Day
import utils.toCharMatrix
import kotlin.math.min

class Day13 : Day<List<Array<CharArray>>>(2023, 13) {

    override fun List<String>.parse(): List<Array<CharArray>> {
        val list = mutableListOf<Array<CharArray>>()
        val lines = mutableListOf<String>()
        forEach {
            if (it.isBlank()) {
                list += lines.toCharMatrix()
                lines.clear()
            } else {
                lines += it
            }
        }
        list += lines.toCharMatrix()
        return list
    }


    override fun solve1(input: List<Array<CharArray>>) = input.sumOf { it.getSplitValue() }

    override fun solve2(input: List<Array<CharArray>>): Int = 0

    private fun Array<CharArray>.getSplitValue() = findVerticalSplit() ?: (findHorizontalSplit()!! * 100)

    private fun Array<CharArray>.findVerticalSplit(): Int? {
        for (index in 1..lastIndex) {
            val range = min(index, lastIndex - index)
            var mirrored = true
            for (distance in 0..range) {
                if (index - distance - 1 < 0) break
                for (y in this[0].indices) {
                    if (this[index - distance - 1][y] != this[index + distance][y]) {
                        mirrored = false
                        break
                    }
                }
                if (!mirrored) break
            }
            if (mirrored) return index
        }
        return null
    }

    private fun Array<CharArray>.findHorizontalSplit(): Int? {
        for (index in 1..this[0].lastIndex) {
            val range = min(index, this[0].lastIndex - index)
            var mirrored = true
            for (distance in 0..range) {
                if (index - distance - 1 < 0) break
                for (x in indices) {
                    if (this[x][index - distance - 1] != this[x][index + distance]) {
                        mirrored = false
                        break
                    }
                }
                if (!mirrored) break
            }
            if (mirrored) return index
        }
        return null
    }
}