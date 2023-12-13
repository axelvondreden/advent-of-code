package y2023

import Day
import utils.copy
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

    override fun solve2(input: List<Array<CharArray>>) = input.sumOf { it.getSecondSplitValue() }

    private fun Array<CharArray>.getSplitValue() = findVerticalSplits().firstOrNull() ?: (findHorizontalSplits().first() * 100)

    private fun Array<CharArray>.getSecondSplitValue(): Int {
        val originalV = findVerticalSplits().firstOrNull()
        val originalH = if (originalV == null) findHorizontalSplits().first() else null
        for (x in indices) {
            for (y in this[x].indices) {
                val copy = copy()
                copy[x][y] = if (copy[x][y] == '#') '.' else '#'
                val newV = copy.findVerticalSplits()
                if (newV != null && newV != originalV) return newV
                val newH = copy.findHorizontalSplits()
                if (newH != null && newH != originalH) return newH * 100
            }
        }
        error("Big Fail")
    }

    private fun Array<CharArray>.findVerticalSplits(): List<Int> {
        val list = mutableListOf<Int>()
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
            if (mirrored) list += index
        }
        return list
    }

    private fun Array<CharArray>.findHorizontalSplits(): List<Int> {
        val list = mutableListOf<Int>()
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
            if (mirrored) list += index
        }
        return list
    }
}