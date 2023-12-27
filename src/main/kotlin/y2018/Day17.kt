package y2018

import Day
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.count
import kotlin.collections.drop
import kotlin.collections.forEach
import kotlin.collections.hashMapOf
import kotlin.collections.maxOf
import kotlin.collections.minOf
import kotlin.collections.set

class Day17 : Day<HashMap<Pair<Int, Int>, Int>>(2018, 17) {

    override suspend fun List<String>.parse(): HashMap<Pair<Int, Int>, Int> {
        val map = hashMapOf<Pair<Int, Int>, Int>()

        val regex = "(\\w)=(\\d+), \\w=(\\d+)..(\\d+)".toRegex()
        mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1) }.forEach { (xy, v, v1, v2) ->
            var x1 = 0
            var x2 = 0
            var y1 = 0
            var y2 = 0
            when (xy) {
                "x" -> {
                    x1 = v.toInt()
                    x2 = v.toInt()
                    y1 = v1.toInt()
                    y2 = v2.toInt()
                }

                "y" -> {
                    y1 = v.toInt()
                    y2 = v.toInt()
                    x1 = v1.toInt()
                    x2 = v2.toInt()
                }
            }

            (x1..x2).forEach { x ->
                (y1..y2).forEach { y ->
                    map[x to y] = WALL
                }
            }
        }
        return map
    }

    override suspend fun solve1(input: HashMap<Pair<Int, Int>, Int>): Int {
        val rangeY = input.keys.minOf { (_, y) -> y }..input.keys.maxOf { (_, y) -> y }

        input.drop(rangeY, 500, 0)
        return input.count { (key, value) -> key.second in rangeY && (value == STILL_WATER || value == FLOW_WATER) }
    }

    override suspend fun solve2(input: HashMap<Pair<Int, Int>, Int>): Int {
        val rangeY = input.keys.minOf { (_, y) -> y }..input.keys.maxOf { (_, y) -> y }

        input.drop(rangeY, 500, 0)
        return input.count { (key, value) -> key.second in rangeY && value == STILL_WATER }
    }

    fun HashMap<Pair<Int, Int>, Int>.drop(rangeY: IntRange, x: Int, y: Int) {
        var ty = y
        while (true) {
            if (ty > rangeY.last) return

            this[x to ty] = FLOW_WATER
            ty++
            when (this[x to ty]) {
                WALL, STILL_WATER -> break
                FLOW_WATER -> return
                else -> {}
            }
        }

        while (true) {
            ty--
            if (ty < y) return
            val leftBorder = scanBorder(-1, x, ty, rangeY)
            val rightBorder = scanBorder(1, x, ty, rangeY)
            if (leftBorder != null && rightBorder != null) {
                (leftBorder + 1 until rightBorder).forEach {
                    this[it to ty] = STILL_WATER
                }
            } else {
                break
            }
        }
    }

    private fun HashMap<Pair<Int, Int>, Int>.scanBorder(direction: Int, baseX: Int, ty: Int, rangeY: IntRange): Int? {
        var tx = baseX
        var drop = false

        while (!drop) {
            tx += direction

            when (this[tx to ty]) {
                WALL -> break
                FLOW_WATER -> {
                    drop = true
                    break
                }

                else -> {
                    this[tx to ty] = FLOW_WATER
                    when (this[tx to ty + 1]) {
                        WALL -> {}
                        STILL_WATER -> {}
                        FLOW_WATER -> drop = true
                        else -> {
                            drop(rangeY, tx, ty + 1)
                            if (this[tx to ty + 1] == FLOW_WATER) {
                                drop = true
                                break
                            }
                        }
                    }
                }
            }
        }
        return tx.takeIf { !drop }
    }

    companion object {
        private const val WALL = 1
        private const val FLOW_WATER = 2
        private const val STILL_WATER = 3
    }
}