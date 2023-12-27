package y2018

import Day
import kotlin.math.absoluteValue

class Day09 : Day<List<String>>(2018, 9) {

    override suspend fun List<String>.parse() = first().split(" ")

    override suspend fun solve1(input: List<String>) = play(input[0].toInt(), input[6].toInt())

    override suspend fun solve2(input: List<String>) = play(input[0].toInt(), input[6].toInt() * 100)

    private fun play(numPlayers: Int, highest: Int): Long {
        val scores = LongArray(numPlayers)
        val marbles = ArrayDeque<Int>().also { it.add(0) }

        (1..highest).forEach { marble ->
            when {
                marble % 23 == 0 -> {
                    scores[marble % numPlayers] += marble + with(marbles) {
                        shift(-7)
                        removeFirst().toLong()
                    }
                    marbles.shift(1)
                }
                else -> {
                    with(marbles) {
                        shift(1)
                        addFirst(marble)
                    }
                }
            }
        }
        return scores.max()
    }

    private fun <T> ArrayDeque<T>.shift(n: Int) = when {
        n < 0 -> repeat(n.absoluteValue) {
            addLast(removeFirst())
        }
        else -> repeat(n) {
            addFirst(removeLast())
        }
    }
}