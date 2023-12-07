package y2017

import Day
import java.math.BigInteger

class Day14 : Day<Any?>(2017, 14) {

    override val input = readString().toBinaryStrings()

    private val grid by lazy { stringsToGrid() }

    override fun solve1(input: List<String>) = input.sumOf { binaryString -> binaryString.count { it == '1' } }

    override fun solve2(input: List<String>): Int {
        var groups = 0
        grid.forEachIndexed { x, row ->
            row.forEachIndexed { y, spot ->
                if (spot == 1) {
                    groups += 1
                    markNeighbors(x, y)
                }
            }
        }
        return groups
    }

    private fun markNeighbors(x: Int, y: Int) {
        if (grid[x][y] == 1) {
            grid[x][y] = 0
            neighborsOf(x, y).forEach {
                markNeighbors(it.first, it.second)
            }
        }
    }

    private fun neighborsOf(x: Int, y: Int) = listOf(Pair(x - 1, y), Pair(x + 1, y), Pair(x, y - 1), Pair(x, y + 1))
        .filter { it.first in 0..127 }
        .filter { it.second in 0..127 }

    private fun stringsToGrid() = input.map { s -> s.map { it.toString().toInt() } }.map { it.toIntArray() }

    private fun String.toBinaryStrings() = (0..127).map { KnotHash.hash("$this-$it") }
        .map { BigInteger(it, 16).toString(2).padStart(128, '0') }

    object KnotHash {

        private val magicLengths = listOf(17, 31, 73, 47, 23)

        fun hash(input: String) = runForLengths(lengthsForString(input)).toList().chunked(16)
            .joinToString("") { it.xor().toHex(2) }

        private fun runForLengths(lengths: IntArray): IntArray {
            val ring = IntArray(256) { it }
            var position = 0
            var skip = 0
            repeat(64) {
                lengths.forEach { length ->
                    reverseSection(ring, position, length)
                    position = (position + length + skip) % ring.size
                    skip += 1
                }
            }
            return ring
        }

        private fun reverseSection(ring: IntArray, from: Int, length: Int) {
            var fromIdx = from % ring.size
            var toIdx = (fromIdx + length - 1) % ring.size
            repeat(length / 2) {
                ring.swap(fromIdx, toIdx)
                fromIdx = fromIdx.inc() % ring.size
                toIdx = toIdx.dec().takeIf { it >= 0 } ?: (ring.size - 1)
            }
        }

        private fun lengthsForString(input: String) = (input.map { it.code } + magicLengths).toIntArray()

        private fun IntArray.swap(a: Int, b: Int): IntArray {
            val tmp = this[a]
            this[a] = this[b]
            this[b] = tmp
            return this
        }

        private fun List<Int>.xor(): Int = this.reduce { a, b -> a xor b }

        private fun Int.toHex(width: Int = 1): String = "%0${width}x".format(this)
    }
}
