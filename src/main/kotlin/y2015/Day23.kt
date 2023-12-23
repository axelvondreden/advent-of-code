package y2015

import Day
import androidx.compose.ui.graphics.Color
import runner.compose.VizGrid

class Day23 : Day<List<String>>(2015, 23) {

    override suspend fun List<String>.parse() = map { it.replace(",", "") }

    var index = 0

    override suspend fun solve1(input: List<String>): Long {
        index = 0
        val register = mutableMapOf("a" to 0L, "b" to 0L)
        while (index in input.indices) {
            register.run(input)
        }
        return register["b"]!!
    }

    override suspend fun solve2(input: List<String>): Long {
        index = 0
        val register = mutableMapOf("a" to 1L, "b" to 0L)
        while (index in input.indices) {
            register.run(input)
        }
        return register["b"]!!
    }

    override fun initViz1(input: List<String>) = VizGrid(width = 25, height = 20).apply {
        index = 0
        text(0, 0, "Cursor:X")
        text(0, 2, "a:0")
        text(0, 3, "b:0")

        (-10..9).forEach {
            if (index + it in input.indices) {
                text(12, 10 + it, input[it])
            }
        }
        backgroundColor(12 to 10, 23 to 10, Color.Gray)
    }

    override fun initViz2(input: List<String>) = VizGrid(width = 25, height = 20).apply {
        index = 0
        text(0, 0, "Cursor:X")
        text(0, 2, "a:0")
        text(0, 3, "b:0")

        (-10..9).forEach {
            if (index + it in input.indices) {
                text(12, 10 + it, input[it])
            }
        }
        backgroundColor(12 to 10, 23 to 10, Color.Gray)
    }

    override suspend fun solve1Visualized(input: List<String>, onProgress: suspend (VizGrid) -> Unit): Long {
        index = 0
        val register = mutableMapOf("a" to 0L, "b" to 0L)
        while (index in input.indices) {
            onProgress(getViz(input, register))
            register.run(input)
        }
        onProgress(getViz(input, register))
        return register["b"]!!
    }

    override suspend fun solve2Visualized(input: List<String>, onProgress: suspend (VizGrid) -> Unit): Long {
        index = 0
        val register = mutableMapOf("a" to 1L, "b" to 0L)
        while (index in input.indices) {
            onProgress(getViz(input, register))
            register.run(input)
        }
        onProgress(getViz(input, register))
        return register["b"]!!
    }

    private fun getViz(input: List<String>, register: Map<String, Long>) = VizGrid(width = 25, height = 20).apply {
        text(0, 0, "Cursor:$index")
        text(0, 2, "a:${register["a"]}")
        text(0, 3, "b:${register["b"]}")

        (-10..9).forEach {
            if (index + it in input.indices) {
                text(12, 10 + it, input[index + it])
            }
        }
        backgroundColor(12 to 10, 23 to 10, Color.Gray)
    }

    private fun MutableMap<String, Long>.run(input: List<String>) {
        val inst = input[index].split(" ")
        when (inst[0]) {
            "hlf" -> {
                put(inst[1], get(inst[1])!! / 2)
                index++
            }
            "tpl" -> {
                put(inst[1], get(inst[1])!! * 3)
                index++
            }
            "inc" -> {
                put(inst[1], get(inst[1])!! + 1)
                index++
            }
            "jmp" -> index += inst[1].toInt()
            "jie" -> if (get(inst[1])!! % 2 == 0L) index += inst[2].toInt() else index++
            "jio" -> if (get(inst[1])!! == 1L) index += inst[2].toInt() else index++
        }
    }
}