package y2016

import Day
import utils.copy

class Day08 : Day(2016, 8) {

    override val input = readStrings().map { it.split(" ") }

    private val screen = Array(50) { BooleanArray(6) }

    init {
        input.forEach(::evaluate)
    }

    override fun solve1() = screen.sumBy { booleans -> booleans.count { it } }

    override fun solve2() = printScreen()

    private fun evaluate(inst: List<String>) {
        val old = screen.copy()
        when (inst[0]) {
            "rect" -> {
                val a = inst[1].split("x")[0].toInt()
                val b = inst[1].split("x")[1].toInt()
                for (x in 0 until a) {
                    for (y in 0 until b) {
                        screen[x][y] = true
                    }
                }
            }
            "rotate" -> {
                when (inst[1]) {
                    "row" -> {
                        val row = inst[2].split("=")[1].toInt()
                        val shift = inst[4].toInt()
                        for (x in screen.indices) {
                            val newX = (x + shift) % screen.size
                            screen[newX][row] = old[x][row]
                        }
                    }
                    "column" -> {
                        val col = inst[2].split("=")[1].toInt()
                        val shift = inst[4].toInt()
                        for (y in screen[0].indices) {
                            val newY = (y + shift) % screen[0].size
                            screen[col][newY] = old[col][y]
                        }
                    }
                }
            }
        }
    }

    private fun printScreen(): String {
        var ret = "\n\t\t"
        for (y in screen[0].indices) {
            for (x in screen.indices) {
                ret += if (screen[x][y]) '#' else ' '
            }
            ret += "\n\t\t"
        }
        return ret
    }
}