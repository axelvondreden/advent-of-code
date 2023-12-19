package y2023

import Day
import utils.copy
import utils.findPoints
import utils.get
import utils.set
import utils.toCharMatrix

class Day14 : Day<Array<CharArray>>(2023, 14) {

    override suspend fun List<String>.parse() = toCharMatrix()

    override suspend fun solve1(input: Array<CharArray>) = input.tiltNorth().sumOf { col ->
        col.withIndex().sumOf { c -> if (c.value == 'O') col.size - c.index else 0 }
    }

    override suspend fun solve2(input: Array<CharArray>): Int {
        var step = 0
        var skip = true
        val seen = hashMapOf<String, Int>()
        var map = input.copy()

        while (step < 1_000_000_000) {
            map = map.spinCycle()
            if (skip) {
                when (val key = map.contentDeepToString()) {
                    in seen -> {
                        val cycle = step - seen.getValue(key)
                        val cyclesLeft = (1_000_000_000 - step) / cycle
                        step += cycle * cyclesLeft
                        skip = false
                    }
                    else -> seen[key] = step
                }
            }

            step++
        }

        return map.sumOf { col -> col.withIndex().sumOf { c -> if (c.value == 'O') col.size - c.index else 0 } }
    }

    private fun Array<CharArray>.spinCycle() = tiltNorth().tiltWest().tiltSouth().tiltEast()

    private fun Array<CharArray>.tiltNorth(): Array<CharArray> {
        var working = true
        val array = copy()
        while (working) {
            working = false
            val stones = array.findPoints('O')
            stones.forEach { stone ->
                if (stone.y > 0 && array[stone up 1] == '.') {
                    working = true
                    array[stone] = '.'
                    array[stone up 1] = 'O'
                }
            }
        }
        return array
    }

    private fun Array<CharArray>.tiltSouth(): Array<CharArray> {
        var working = true
        val array = copy()
        while (working) {
            working = false
            val stones = array.findPoints('O')
            stones.forEach { stone ->
                if (stone.y < array[0].lastIndex && array[stone down 1] == '.') {
                    working = true
                    array[stone] = '.'
                    array[stone down 1] = 'O'
                }
            }
        }
        return array
    }

    private fun Array<CharArray>.tiltWest(): Array<CharArray> {
        var working = true
        val array = copy()
        while (working) {
            working = false
            val stones = array.findPoints('O')
            stones.forEach { stone ->
                if (stone.x > 0 && array[stone left 1] == '.') {
                    working = true
                    array[stone] = '.'
                    array[stone left 1] = 'O'
                }
            }
        }
        return array
    }

    private fun Array<CharArray>.tiltEast(): Array<CharArray> {
        var working = true
        val array = copy()
        while (working) {
            working = false
            val stones = array.findPoints('O')
            stones.forEach { stone ->
                if (stone.x < array.lastIndex && array[stone right 1] == '.') {
                    working = true
                    array[stone] = '.'
                    array[stone right 1] = 'O'
                }
            }
        }
        return array
    }
}