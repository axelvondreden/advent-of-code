package y2018

import Day
import utils.Dir
import utils.Point
import utils.Turn

class Day13 : Day(2018, 13) {

    override val input = readCharMatrix()

    private val map = input.map { chars ->
        chars.map {
            when (it) {
                '<', '>' -> '-'
                '^', 'v' -> '|'
                else -> it
            }
        }.toCharArray()
    }.toTypedArray()

    override fun solve1(): String {
        val carts = createCarts()
        while (true) {
            carts.sortedBy { (it.y * 10000) + it.x }.forEach { cart ->
                cart.step()
                val collisions = carts.getCollision()
                if (collisions.isNotEmpty()) {
                    val p = collisions.first()
                    return "${p.x},${p.y}"
                }
            }
        }
    }

    override fun solve2(): String {
        val carts = createCarts()
        while (true) {
            carts.sortedBy { (it.y * 10000) + it.x }.forEach { cart ->
                if (cart.active) {
                    cart.step()
                    carts.getCollision().forEach { point ->
                        carts.filter { it.x == point.x.toInt() && it.y == point.y.toInt() }.forEach {
                            it.active = false
                        }
                    }
                }
            }
            if (carts.count { it.active } == 1) {
                return carts.first { it.active }.let { "${it.x},${it.y}" }
            }
        }
    }

    private fun createCarts(): List<Cart> {
        val list = mutableListOf<Cart>()
        input.indices.forEach { x ->
            input[x].indices.forEach { y ->
                when (input[x][y]) {
                    '<' -> list += Cart(x, y, Dir.LEFT, map)
                    '>' -> list += Cart(x, y, Dir.RIGHT, map)
                    '^' -> list += Cart(x, y, Dir.UP, map)
                    'v' -> list += Cart(x, y, Dir.DOWN, map)
                }
            }
        }
        return list
    }

    private fun List<Cart>.getCollision() =
        filter { it.active }.map { Point(it.x, it.y) }.groupBy { it }.filterValues { it.size > 1 }.keys

    private class Cart(var x: Int, var y: Int, var dir: Dir, private val map: Array<CharArray>) {
        private var nextTurn = Turn.LEFT
        var active = true

        fun step() {
            when (dir) {
                Dir.UP -> y -= 1
                Dir.DOWN -> y += 1
                Dir.LEFT -> x -= 1
                Dir.RIGHT -> x += 1
            }
            val c = map[x][y]
            dir = when (c) {
                '\\' -> when (dir) {
                    Dir.UP, Dir.DOWN -> dir.turn(Turn.LEFT)
                    Dir.LEFT, Dir.RIGHT -> dir.turn(Turn.RIGHT)
                }
                '/' -> when (dir) {
                    Dir.UP, Dir.DOWN -> dir.turn(Turn.RIGHT)
                    Dir.LEFT, Dir.RIGHT -> dir.turn(Turn.LEFT)
                }
                '+' -> {
                    val newDir = dir.turn(nextTurn)
                    nextTurn = when (nextTurn) {
                        Turn.LEFT -> Turn.STRAIGHT
                        Turn.STRAIGHT -> Turn.RIGHT
                        Turn.RIGHT -> Turn.LEFT
                    }
                    newDir
                }
                else -> dir
            }
        }
    }
}