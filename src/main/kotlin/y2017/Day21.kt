package y2017

import Day

class Day21 : Day<List<Day21.Pattern>>(2017, 21) {

    override suspend fun List<String>.parse() = parsePatterns()

    private val image = arrayOf(
        charArrayOf('.', '#', '.'),
        charArrayOf('.', '.', '#'),
        charArrayOf('#', '#', '#')
    )

    override suspend fun solve1(input: List<Pattern>): Int {
        var copy = image.copyOf()
        repeat(5) { copy = copy.step(input) }
        return copy.sumOf { chars -> chars.count { it == '#' } }
    }

    override suspend fun solve2(input: List<Pattern>): Int {
        var copy = image.copyOf()
        repeat(18) { copy = copy.step(input) }
        return copy.sumOf { chars -> chars.count { it == '#' } }
    }

    private fun Array<CharArray>.step(input: List<Pattern>) = breakUp(if (size % 2 == 0) 2 else 3).map { imgCol ->
        imgCol.map { it.convert(input) }.toTypedArray()
    }.toTypedArray().join()

    private fun Array<CharArray>.convert(input: List<Pattern>) = input.first { it.input.contentDeepEquals(this) }.output

    private fun Array<CharArray>.breakUp(blockSize: Int): Array<Array<Array<CharArray>>> {
        val ret = Array(size / blockSize) { Array(size / blockSize) { Array(blockSize) { CharArray(blockSize) } } }
        indices.forEach { x ->
            indices.forEach { y ->
                ret[x / blockSize][y / blockSize][x % blockSize][y % blockSize] = this[x][y]
            }
        }
        return ret
    }

    private fun Array<Array<Array<CharArray>>>.join(): Array<CharArray> {
        val blockSize = this[0][0].size
        val ret = Array(size * blockSize) { CharArray(size * blockSize) }
        (0 until blockSize * size).forEach { x ->
            (0 until blockSize * size).forEach { y ->
                ret[x][y] = this[x / blockSize][y / blockSize][x % blockSize][y % blockSize]
            }
        }
        return ret
    }

    private fun Array<CharArray>.rotateClockwise() = mapIndexed { x, row ->
        row.mapIndexed { y, _ -> this[y][x] }.reversed().toCharArray()
    }.toTypedArray()

    private fun Array<CharArray>.flipped() = map { it.reversed().toCharArray() }.toTypedArray()

    private fun Array<CharArray>.orientations(): Set<Array<CharArray>> {
        var copy = this.copyOf()
        val list = mutableSetOf(copy)
        repeat(3) {
            copy = copy.rotateClockwise()
            list.add(copy)
        }
        copy = copy.flipped()
        list.add(copy)
        repeat(3) {
            copy = copy.rotateClockwise()
            list.add(copy)
        }
        return list
    }

    private fun List<String>.parsePatterns() = flatMap { line ->
        val split = line.split(" => ")
        val input = split[0].split("/").map { row -> row.toCharArray() }.toTypedArray()
        val output = split[1].split("/").map { row -> row.toCharArray() }.toTypedArray()
        input.orientations().toList().map {
            Pattern(it, output)
        }
    }

    data class Pattern(val input: Array<CharArray>, val output: Array<CharArray>) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Pattern
            if (!input.contentDeepEquals(other.input)) return false
            if (!output.contentDeepEquals(other.output)) return false
            return true
        }

        override fun hashCode() = 31 * input.contentDeepHashCode() + output.contentDeepHashCode()
    }
}
