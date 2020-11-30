package y2015

import Day
import Utils
import copy

class Day18 : Day() {

    override val input = Utils.readCharMatrix(2015, 18).map { chars -> chars.map { it == '#' }.toBooleanArray() }.toTypedArray()

    override fun solve1(): Int {
        var map = input.copy()
        repeat(100) {
            val temp = Array(map.size) { BooleanArray(map[0].size) }
            for (x in temp.indices) {
                for (y in temp[0].indices) {
                    temp[x][y] = getNextState(x, y)
                }
            }
            map = temp
        }
        return map.sumBy { booleans -> booleans.count { it } }
    }

    override fun solve2(): Int {
        var map = input.copy()
        map[0][0] = true
        map[map.size - 1][0] = true
        map[0][map.size - 1] = true
        map[map.size - 1][map.size - 1] = true
        repeat(100) {
            val temp = Array(map.size) { BooleanArray(map[0].size) }
            for (x in temp.indices) {
                for (y in temp[0].indices) {
                    temp[x][y] = getNextState(x, y)
                }
            }
            temp[0][0] = true
            temp[temp.size - 1][0] = true
            temp[0][temp.size - 1] = true
            temp[temp.size - 1][temp.size - 1] = true
            map = temp
        }
        return map.sumBy { booleans -> booleans.count { it } }
    }

    private fun getNextState(x: Int, y: Int): Boolean {
        val onCount = getNeighbors(x, y).count { it }
        return if (input[x][y]) {
            onCount == 2 || onCount == 3
        } else {
            onCount == 3
        }
    }

    private fun getNeighbors(x: Int, y: Int): List<Boolean> {
        val list = mutableListOf<Boolean>()
        for (xx in x - 1..x + 1) {
            for (yy in y - 1..y + 1) {
                if (xx != x || yy != y) {
                    try {
                        list.add(input[xx][yy])
                    } catch (e: ArrayIndexOutOfBoundsException) {
                    }
                }
            }
        }
        return list
    }
}