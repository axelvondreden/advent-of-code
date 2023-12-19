package y2021

import Day
import utils.toCharMatrix
import kotlin.math.min

class Day15 : Day<Array<IntArray>>(2021, 15) {

    override suspend fun List<String>.parse() = toCharMatrix().map { chars -> chars.map { it.digitToInt() }.toIntArray() }.toTypedArray()

    override suspend fun solve1(input: Array<IntArray>): Int {
        return minPathCost(input)
    }

    override suspend fun solve2(input: Array<IntArray>): Int {
        val expandedMap = Array(input.size * 5) { x ->
            IntArray(input[0].size * 5) { y ->
                val xAdd = x / input.size
                val yAdd = y / input[0].size
                val mx = x % input.size
                val my = y % input[0].size
                val oldValue = input[mx][my]
                val newValue = oldValue + xAdd + yAdd
                if (newValue > 9) newValue - 9 else newValue
            }
        }
        return minPathCost(expandedMap)
    }

    private fun minPathCost(matrix: Array<IntArray>): Int {
        val rows = matrix.size
        val cols = matrix[0].size
        val dp = Array(rows) { IntArray(cols) }

        dp[0][0] = 0

        for (i in 1 until rows)
            dp[i][0] = dp[i - 1][0] + matrix[i][0]

        for (j in 1 until cols)
            dp[0][j] = dp[0][j - 1] + matrix[0][j]

        for (i in 1 until rows) {
            for (j in 1 until cols) {
                dp[i][j] = matrix[i][j] + min(dp[i - 1][j], dp[i][j - 1])
            }
        }

        return dp[rows - 1][cols - 1]
    }
}