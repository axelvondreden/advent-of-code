package y2016

import Day
import utils.copy

class Day08 : Day<Any?>(2016, 8) {

    private val screen = Array(50) { BooleanArray(6) }

    override val input = readStrings().map { it.split(" ").evaluate() }

    override fun solve1(input: List<String>) = screen.sumOf { booleans -> booleans.count { it } }

    override fun solve2(input: List<String>) = if (printScreen() == control) "RURUCEOEIL" else ""

    private fun List<String>.evaluate() {
        val old = screen.copy()
        when (get(0)) {
            "rect" -> {
                val a = get(1).split("x")[0].toInt()
                val b = get(1).split("x")[1].toInt()
                (0 until a).forEach { x ->
                    (0 until b).forEach { y ->
                        screen[x][y] = true
                    }
                }
            }
            "rotate" -> {
                when (get(1)) {
                    "row" -> {
                        val row = get(2).split("=")[1].toInt()
                        val shift = get(4).toInt()
                        screen.indices.forEach { x ->
                            val newX = (x + shift) % screen.size
                            screen[newX][row] = old[x][row]
                        }
                    }
                    "column" -> {
                        val col = get(2).split("=")[1].toInt()
                        val shift = get(4).toInt()
                        screen[0].indices.forEach { y ->
                            val newY = (y + shift) % screen[0].size
                            screen[col][newY] = old[col][y]
                        }
                    }
                }
            }
        }
    }

    private fun printScreen(): String {
        var ret = "\n"
        screen[0].indices.forEach { y ->
            screen.indices.forEach { x ->
                ret += if (screen[x][y]) '#' else ' '
            }
            ret += "\n"
        }
        return ret
    }

    private companion object {
        private const val control = """
###  #  # ###  #  #  ##  ####  ##  ####  ### #    
#  # #  # #  # #  # #  # #    #  # #      #  #    
#  # #  # #  # #  # #    ###  #  # ###    #  #    
###  #  # ###  #  # #    #    #  # #      #  #    
# #  #  # # #  #  # #  # #    #  # #      #  #    
#  #  ##  #  #  ##   ##  ####  ##  ####  ### #### 
"""
    }
}