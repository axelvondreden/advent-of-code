package y2016

import Day
import utils.copy

class Day08 : Day<List<Day08.Instruction>>(2016, 8) {

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

    sealed class Instruction {
        abstract fun apply(map: Array<BooleanArray>)

        data class Rect(val a: Int, val b: Int) : Instruction() {
            override fun apply(map: Array<BooleanArray>) {
                (0 until a).forEach { x ->
                    (0 until b).forEach { y ->
                        map[x][y] = true
                    }
                }
            }
        }
        data class RotateRow(val y: Int, val shift: Int) : Instruction() {
            override fun apply(map: Array<BooleanArray>) {
                val old = map.copy()
                map.indices.forEach { x ->
                    val newX = (x + shift) % map.size
                    map[newX][y] = old[x][y]
                }
            }
        }
        data class RotateColumn(val x: Int, val shift: Int) : Instruction() {
            override fun apply(map: Array<BooleanArray>) {
                val old = map.copy()
                map[0].indices.forEach { y ->
                    val newY = (y + shift) % map[0].size
                    map[x][newY] = old[x][y]
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