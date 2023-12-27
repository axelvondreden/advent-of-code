package y2016

import DayViz
import runner.compose.VizGrid


class Day18 : DayViz<String, VizGrid>(2016, 18) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String) = input.count(40)

    override suspend fun solve2(input: String) = input.count(400000)

    override fun initViz1(input: String) = VizGrid(null, input.length, 20).apply {
        text(0, 0, input) }

    override fun initViz2(input: String) = VizGrid(null, input.length, 20).apply {
        text(0, 0, input)
    }

    override suspend fun solve1Visualized(input: String, onProgress: suspend (VizGrid) -> Unit) =
        input.count(40, onProgress)

    override suspend fun solve2Visualized(input: String, onProgress: suspend (VizGrid) -> Unit) =
        input.count(400000, onProgress)

    private suspend fun String.count(rows: Int, onProgress: (suspend (VizGrid) -> Unit)? = null): Int {
        var currentRow = this
        val map = mutableListOf<String>()
        repeat(rows) {
            map.add(currentRow)
            currentRow = currentRow.nextRow()
            if (onProgress != null) {
                onProgress(VizGrid((it + 1).toDouble() / rows, length, 20).apply {
                    map.takeLast(20).forEachIndexed { index, s ->
                        text(0, index, s)
                    }
                })
            }
        }
        return map.sumOf { row -> row.count { it == '.' } }
    }

    private fun String.nextRow(): String {
        val new = StringBuilder()
        while (new.length < length) {
            val index = new.length
            val left = index > 0 && get(index - 1) == '^'
            val center = get(index) == '^'
            val right = index < length - 1 && get(index + 1) == '^'
            new.append(if (isTrap(left, center, right)) '^' else '.')
        }
        return new.toString()
    }

    private fun isTrap(left: Boolean, center: Boolean, right: Boolean): Boolean {
        if (left && center && !right) return true
        if (center && right && !left) return true
        if (left && !center && !right) return true
        if (!left && !center && right) return true
        return false
    }
}