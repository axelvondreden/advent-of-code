package y2019

import Day
import utils.copy
import kotlin.math.pow

class Day24 : Day(2019, 24) {

    override val input = readCharMatrix()

    override fun solve1(): Int {
        val states = mutableSetOf(input.toFlatString())
        var new = step(input.copy())
        while (!states.contains(new.toFlatString())) {
            states.add(new.toFlatString())
            new = step(new)
        }
        return new.toFlatString().mapIndexed { i, c -> if (c == '#') 2.toDouble().pow(i).toInt() else 0 }.sum()
    }

    override fun solve2(): Int {
        var map = mutableMapOf<Int, Array<CharArray>>()
        (-200..200).forEach { map[it] = Array(5) { CharArray(5) { '.' } } }
        map[0] = input.copy()
        repeat(200) { map = stepRecursive(map) }
        return map.values.sumOf { arrayOfCharArrays -> arrayOfCharArrays.sumOf { chars -> chars.count { it == '#' } } }
    }

    private fun step(map: Array<CharArray>): Array<CharArray> {
        val new = map.copy()
        new[0].indices.forEach { y ->
            new.indices.forEach { x ->
                val n = map.countNeighbors(x, y)
                new[x][y] = if (map[x][y] == '#') {
                    if (n == 1) '#' else '.'
                } else {
                    if (n == 1 || n == 2) '#' else '.'
                }
            }
        }
        return new
    }

    private fun stepRecursive(map: MutableMap<Int, Array<CharArray>>): MutableMap<Int, Array<CharArray>> {
        val minLvl = map.keys.minOrNull()!!
        val maxLvl = map.keys.maxOrNull()!!
        val ref = mutableMapOf<Int, Array<CharArray>>()
        (minLvl..maxLvl).forEach {
            ref[it] = map[it]!!.copy()
        }
        (minLvl..maxLvl).forEach { lvl ->
            ref.getValue(lvl)[0].indices.forEach { y ->
                ref.getValue(lvl).indices.forEach { x ->
                    val n = countNeighborsRecursive(ref, lvl, x, y)
                    map.getValue(lvl)[x][y] = if (ref.getValue(lvl)[x][y] == '#') {
                        if (n == 1) '#' else '.'
                    } else {
                        if (n == 1 || n == 2) '#' else '.'
                    }
                }
            }
        }
        return map
    }

    private fun countNeighborsRecursive(map: Map<Int, Array<CharArray>>, lvl: Int, x: Int, y: Int): Int {
        var count = 0
        val base = map.getValue(lvl)
        when {
            x == 0 && y == 0 -> {
                count += if (base[1][0] == '#') 1 else 0
                count += if (base[0][1] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[1][2] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[2][1] == '#') 1 else 0
            }
            x == 4 && y == 0 -> {
                count += if (base[3][0] == '#') 1 else 0
                count += if (base[4][1] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[2][1] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[3][2] == '#') 1 else 0
            }
            x == 0 && y == 4 -> {
                count += if (base[1][4] == '#') 1 else 0
                count += if (base[0][3] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[1][2] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[2][3] == '#') 1 else 0
            }
            x == 4 && y == 4 -> {
                count += if (base[3][4] == '#') 1 else 0
                count += if (base[4][3] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[3][2] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[2][3] == '#') 1 else 0
            }
            x == 0 -> {
                count += if (base[0][y - 1] == '#') 1 else 0
                count += if (base[0][y + 1] == '#') 1 else 0
                count += if (base[1][y] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[1][2] == '#') 1 else 0
            }
            x == 4 -> {
                count += if (base[4][y - 1] == '#') 1 else 0
                count += if (base[4][y + 1] == '#') 1 else 0
                count += if (base[3][y] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[3][2] == '#') 1 else 0
            }
            y == 0 -> {
                count += if (base[x - 1][0] == '#') 1 else 0
                count += if (base[x + 1][0] == '#') 1 else 0
                count += if (base[x][1] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[2][1] == '#') 1 else 0
            }
            y == 4 -> {
                count += if (base[x - 1][4] == '#') 1 else 0
                count += if (base[x + 1][4] == '#') 1 else 0
                count += if (base[x][3] == '#') 1 else 0
                count += if (map.containsKey(lvl - 1) && map.getValue(lvl - 1)[2][3] == '#') 1 else 0
            }
            x == 2 && y == 1 -> {
                count += if (base[1][1] == '#') 1 else 0
                count += if (base[3][1] == '#') 1 else 0
                count += if (base[2][0] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[0][0] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[1][0] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[2][0] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[3][0] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[4][0] == '#') 1 else 0
            }
            x == 2 && y == 3 -> {
                count += if (base[1][3] == '#') 1 else 0
                count += if (base[3][3] == '#') 1 else 0
                count += if (base[2][4] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[0][4] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[1][4] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[2][4] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[3][4] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[4][4] == '#') 1 else 0
            }
            x == 1 && y == 2 -> {
                count += if (base[1][1] == '#') 1 else 0
                count += if (base[1][3] == '#') 1 else 0
                count += if (base[0][2] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[0][0] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[0][1] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[0][2] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[0][3] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[0][4] == '#') 1 else 0
            }
            x == 3 && y == 2 -> {
                count += if (base[3][1] == '#') 1 else 0
                count += if (base[3][3] == '#') 1 else 0
                count += if (base[4][2] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[4][0] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[4][1] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[4][2] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[4][3] == '#') 1 else 0
                count += if (map.containsKey(lvl + 1) && map.getValue(lvl + 1)[4][4] == '#') 1 else 0
            }
            x != 2 || y != 2 -> {
                count += if (base[x - 1][y] == '#') 1 else 0
                count += if (base[x + 1][y] == '#') 1 else 0
                count += if (base[x][y - 1] == '#') 1 else 0
                count += if (base[x][y + 1] == '#') 1 else 0
            }
        }
        return count
    }

    private fun Array<CharArray>.countNeighbors(x: Int, y: Int): Int {
        var count = 0
        if (x > 0) count += if (get(x - 1)[y] == '#') 1 else 0
        if (y > 0) count += if (get(x)[y - 1] == '#') 1 else 0
        if (x < size - 1) count += if (get(x + 1)[y] == '#') 1 else 0
        if (y < get(0).size - 1) count += if (get(x)[y + 1] == '#') 1 else 0
        return count
    }

    private fun Array<CharArray>.toFlatString(): String {
        var ret = ""
        get(0).indices.forEach { y ->
            indices.forEach { x ->
                ret += get(x)[y]
            }
        }
        return ret
    }
}