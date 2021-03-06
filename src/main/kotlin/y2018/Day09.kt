package y2018

import Day

class Day09 : Day(2018, 9) {

    override val input = readString().split(" ")

    private val playerCount = input[0].toInt()
    private val marbleCount = input[6].toInt()

    override fun solve1() = getMaxScore(playerCount, marbleCount)

    override fun solve2() = getMaxScore(playerCount, marbleCount * 100)

    private fun getMaxScore(playerCount: Int, marbleCount: Int): Long {
        var playerIndex = 0
        val players = List(playerCount) { Player() }
        val unplayedMarbles = MutableList(marbleCount) { it + 1 }
        val board = ArrayDeque<Int>(marbleCount).also { it.addFirst(0) }
        var currentIndex = 0
        while (unplayedMarbles.isNotEmpty()) {
            currentIndex = players[playerIndex].turn(board, currentIndex, unplayedMarbles)
            playerIndex++
            if (playerIndex >= playerCount) playerIndex = 0
        }
        return players.maxOf { it.score }
    }

    class Player(var score: Long = 0) {
        fun turn(board: ArrayDeque<Int>, currentIndex: Int, unplayedMarbles: MutableList<Int>): Int {
            val lowest = unplayedMarbles.removeAt(0)
            return if (lowest % 23 == 0) {
                score += lowest
                val removeIndex = (board.size + (currentIndex - 7)) % board.size
                score += board.removeAt(removeIndex)
                removeIndex
            } else {
                val nextIndex = (currentIndex + 2) % board.size
                board.add(nextIndex, lowest)
                nextIndex
            }
        }
    }
}