package y2016

import DayViz
import runner.compose.VizGrid

class Day12 : DayViz<List<String>, VizGrid>(2016, 12) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val register = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)
        Assembunny(register).run(input.toMutableList())
        return register["a"]!!
    }

    override suspend fun solve2(input: List<String>): Int {
        val register = mutableMapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0)
        Assembunny(register).run(input.toMutableList())
        return register["a"]!!
    }

    override fun initViz1(input: List<String>) = VizGrid(null, 20, 20)

    override fun initViz2(input: List<String>) = VizGrid(null, 20, 20)

    override suspend fun solve1Visualized(input: List<String>, onProgress: suspend (VizGrid) -> Unit): Any {
        val register = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)
        Assembunny(register).runViz(input.toMutableList(), onProgress = onProgress)
        return register["a"]!!
    }

    override suspend fun solve2Visualized(input: List<String>, onProgress: suspend (VizGrid) -> Unit): Any {
        val register = mutableMapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0)
        Assembunny(register).runViz(input.toMutableList(), onProgress = onProgress)
        return register["a"]!!
    }
}