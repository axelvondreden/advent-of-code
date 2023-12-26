package y2016

import DayViz
import androidx.compose.ui.graphics.Color
import runner.compose.Tile
import runner.compose.VizGrid
import utils.copy

class Day08 : DayViz<List<Day08.Instruction>, VizGrid>(2016, 8) {

    override val vizDelay: Long
        get() = 10

    override suspend fun List<String>.parse() = map { line ->
        line.split(" ").let { s ->
            when (s[0]) {
                "rect" -> s[1].split("x").let { Instruction.Rect(it[0].toInt(), it[1].toInt()) }
                "rotate" -> when (s[1]) {
                    "row" -> Instruction.RotateRow(s[2].split("=")[1].toInt(), s[4].toInt())
                    "column" -> Instruction.RotateColumn(s[2].split("=")[1].toInt(), s[4].toInt())
                    else -> error("big fail")
                }

                else -> error("big fail")
            }
        }
    }

    override suspend fun solve1(input: List<Instruction>): Int {
        val screen = Array(50) { BooleanArray(6) }
        input.forEach {
            it.apply(screen)
        }
        return screen.sumOf { booleans -> booleans.count { it } }
    }

    override suspend fun solve2(input: List<Instruction>): String {
        val screen = Array(50) { BooleanArray(6) }
        input.forEach {
            it.apply(screen)
        }
        return if (screen.printScreen() == CONTROL) "RURUCEOEIL" else ""
    }

    override fun initViz1(input: List<Instruction>) = VizGrid(null, 50, 20)

    override suspend fun solve1Visualized(input: List<Instruction>, onProgress: suspend (VizGrid) -> Unit): Int {
        val screen = Array(50) { BooleanArray(6) }
        input.forEachIndexed { index, it ->
            val progress = (index + 1).toDouble() / input.size
            it.apply(screen, progress, onProgress)
        }
        onProgress(getMapViz(1.0, screen))
        return screen.sumOf { booleans -> booleans.count { it } }
    }

    sealed class Instruction {
        abstract suspend fun apply(
            map: Array<BooleanArray>,
            progress: Double? = null,
            onProgress: (suspend (VizGrid) -> Unit)? = null
        )

        data class Rect(val a: Int, val b: Int) : Instruction() {
            override suspend fun apply(
                map: Array<BooleanArray>,
                progress: Double?,
                onProgress: (suspend (VizGrid) -> Unit)?
            ) {
                (0 until a).forEach { x ->
                    (0 until b).forEach { y ->
                        map[x][y] = true
                        if (progress != null && onProgress != null) {
                            onProgress(getMapViz(progress, map))
                        }
                    }
                }
            }
        }

        data class RotateRow(val y: Int, val shift: Int) : Instruction() {
            override suspend fun apply(
                map: Array<BooleanArray>,
                progress: Double?,
                onProgress: (suspend (VizGrid) -> Unit)?
            ) {
                val old = map.copy()
                map.indices.forEach { x ->
                    val newX = (x + shift) % map.size
                    map[newX][y] = old[x][y]
                    if (progress != null && onProgress != null) {
                        onProgress(getMapViz(progress, map))
                    }
                }
            }
        }

        data class RotateColumn(val x: Int, val shift: Int) : Instruction() {
            override suspend fun apply(
                map: Array<BooleanArray>,
                progress: Double?,
                onProgress: (suspend (VizGrid) -> Unit)?
            ) {
                val old = map.copy()
                map[0].indices.forEach { y ->
                    val newY = (y + shift) % map[0].size
                    map[x][newY] = old[x][y]
                    if (progress != null && onProgress != null) {
                        onProgress(getMapViz(progress, map))
                    }
                }
            }
        }
    }

    private fun Array<BooleanArray>.printScreen(): String {
        var ret = "\n"
        this[0].indices.forEach { y ->
            indices.forEach { x ->
                ret += if (this[x][y]) '#' else ' '
            }
            ret += "\n"
        }
        return ret
    }

    private companion object {
        fun getMapViz(progress: Double, screen: Array<BooleanArray>): VizGrid {
            val map = Array(50) { Array(6) { Tile() } }
            (0 until 50).forEach { x ->
                (0 until 6).forEach { y ->
                    with(map[x][y]) {
                        backgroundColor = if (screen[x][y]) Color.White else Color.DarkGray
                    }
                }
            }
            return VizGrid(progress, 50, 20).apply {
                grid(0, 8, map)
            }
        }

        private const val CONTROL = """
###  #  # ###  #  #  ##  ####  ##  ####  ### #    
#  # #  # #  # #  # #  # #    #  # #      #  #    
#  # #  # #  # #  # #    ###  #  # ###    #  #    
###  #  # ###  #  # #    #    #  # #      #  #    
# #  #  # # #  #  # #  # #    #  # #      #  #    
#  #  ##  #  #  ##   ##  ####  ##  ####  ### #### 
"""
    }
}