package y2024

import Day

class Day04 : Day<Array<CharArray>>(2024, 4) {

    override suspend fun List<String>.parse() = map { it.toCharArray() }.toTypedArray()

    override suspend fun solve1(input: Array<CharArray>) = input.indices.sumOf { y ->
        input[y].indices.sumOf { x ->
            if (input[y][x] == 'X') searchXMAS(IntPoint(x, y), input) else 0
        }
    }

    override suspend fun solve2(input: Array<CharArray>) = input.indices.sumOf { y ->
        input[y].indices.count { x -> isX_Mas(IntPoint(x, y), input) }
    }

    private data class IntPoint(val x: Int, val y: Int)

    private fun searchXMAS(start: IntPoint, map: Array<CharArray>): Int {
        return searchR(start, map) + searchL(start, map) + searchU(start, map) + searchD(start, map) +
                searchUL(start, map) + searchDL(start, map) + searchUR(start, map) + searchDR(start, map)
    }

    private fun searchR(p: IntPoint, map: Array<CharArray>): Int {
        if (p.x + 3 <= map[p.y].lastIndex) {
            if (map[p.y][p.x + 1] == 'M' && map[p.y][p.x + 2] == 'A' && map[p.y][p.x + 3] == 'S') {
                return 1
            }
        }
        return 0
    }

    private fun searchL(p: IntPoint, map: Array<CharArray>): Int {
        if (p.x - 3 >= 0) {
            if (map[p.y][p.x - 1] == 'M' && map[p.y][p.x - 2] == 'A' && map[p.y][p.x - 3] == 'S') {
                return 1
            }
        }
        return 0
    }

    private fun searchU(p: IntPoint, map: Array<CharArray>): Int {
        if (p.y - 3 >= 0) {
            if (map[p.y - 1][p.x] == 'M' && map[p.y - 2][p.x] == 'A' && map[p.y - 3][p.x] == 'S') {
                return 1
            }
        }
        return 0
    }

    private fun searchD(p: IntPoint, map: Array<CharArray>): Int {
        if (p.y + 3 <= map.lastIndex) {
            if (map[p.y + 1][p.x] == 'M' && map[p.y + 2][p.x] == 'A' && map[p.y + 3][p.x] == 'S') {
                return 1
            }
        }
        return 0
    }

    private fun searchUL(p: IntPoint, map: Array<CharArray>): Int {
        if (p.y - 3 >= 0 && p.x - 3 >= 0) {
            if (map[p.y - 1][p.x - 1] == 'M' && map[p.y - 2][p.x - 2] == 'A' && map[p.y - 3][p.x - 3] == 'S') {
                return 1
            }
        }
        return 0
    }

    private fun searchDL(p: IntPoint, map: Array<CharArray>): Int {
        if (p.y + 3 <= map.lastIndex && p.x - 3 >= 0) {
            if (map[p.y + 1][p.x - 1] == 'M' && map[p.y + 2][p.x - 2] == 'A' && map[p.y + 3][p.x - 3] == 'S') {
                return 1
            }
        }
        return 0
    }

    private fun searchUR(p: IntPoint, map: Array<CharArray>): Int {
        if (p.y - 3 >= 0 && p.x + 3 <= map[p.y].lastIndex) {
            if (map[p.y - 1][p.x + 1] == 'M' && map[p.y - 2][p.x + 2] == 'A' && map[p.y - 3][p.x + 3] == 'S') {
                return 1
            }
        }
        return 0
    }

    private fun searchDR(p: IntPoint, map: Array<CharArray>): Int {
        if (p.y + 3 <= map.lastIndex && p.x + 3 <= map[p.y].lastIndex) {
            if (map[p.y + 1][p.x + 1] == 'M' && map[p.y + 2][p.x + 2] == 'A' && map[p.y + 3][p.x + 3] == 'S') {
                return 1
            }
        }
        return 0
    }

    private fun isX_Mas(p: IntPoint, map: Array<CharArray>): Boolean {
        if (map[p.y][p.x] != 'A') return false
        if (p.x - 1 >= 0 && p.y - 1 >= 0 && p.x + 1 <= map[p.y].lastIndex && p.y + 1 <= map.lastIndex) {
            val ul = map[p.y - 1][p.x - 1]
            val ur = map[p.y - 1][p.x + 1]
            val dl = map[p.y + 1][p.x - 1]
            val dr = map[p.y + 1][p.x + 1]
            return ((ul == 'S' && dr == 'M') || (ul == 'M' && dr == 'S')) && ((ur == 'S' && dl == 'M') || (ur == 'M' && dl == 'S'))
        }
        return false
    }
}