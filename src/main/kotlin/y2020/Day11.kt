package y2020

import Day
import utils.deepEquals

class Day11 : Day<Any?>(2020, 11) {

    override val input = readCharMatrix()

    override fun solve1(input: List<String>): Int {
        var last = input
        var current = last.step()
        while (!current.deepEquals(last)) {
            last = current
            current = current.step()
        }
        return current.sumOf { it.count { seat -> seat == '#' } }
    }

    override fun solve2(input: List<String>): Int {
        var last = input
        var current = last.step2()
        while (!current.deepEquals(last)) {
            last = current
            current = current.step2()
        }
        return current.sumOf { it.count { seat -> seat == '#' } }
    }

    private fun Array<CharArray>.step() = Array(size) { y ->
        CharArray(get(0).size) { x ->
            val c = get(y)[x]
            val n = countOccupiedNeighbours(x, y)
            when {
                c == 'L' && n == 0 -> '#'
                c == '#' && n >= 4 -> 'L'
                else -> c
            }
        }
    }

    private fun Array<CharArray>.step2() = Array(size) { y ->
        CharArray(get(0).size) { x ->
            val c = get(y)[x]
            val n = countOccupiedVisible(x, y)
            when {
                c == 'L' && n == 0 -> '#'
                c == '#' && n >= 5 -> 'L'
                else -> c
            }
        }
    }

    private fun Array<CharArray>.countOccupiedNeighbours(x: Int, y: Int) = ((x - 1)..(x + 1)).sumOf { xx ->
        ((y - 1)..(y + 1)).count { yy ->
            xx >= 0 && yy >= 0 && xx < get(0).size && yy < size && (x != xx || y != yy) && get(yy)[xx] == '#'
        }
    }

    private fun Array<CharArray>.countOccupiedVisible(x: Int, y: Int) = (-1..1).sumOf { dx ->
        (-1..1).count { dy ->
            (dx != 0 || dy != 0) && isVisibleSeatOccupied(x, y, dx, dy)
        }
    }

    private fun Array<CharArray>.isVisibleSeatOccupied(x: Int, y: Int, dx: Int, dy: Int): Boolean {
        var xx = x + dx
        var yy = y + dy
        while (xx >= 0 && yy >= 0 && xx < get(0).size && yy < size) {
            if (get(yy)[xx] == '#') return true
            if (get(yy)[xx] == 'L') return false
            xx += dx
            yy += dy
        }
        return false
    }
}