package y2020

import Day


class Day22 : Day<List<String>>(2020, 22) {

    override val input = readStrings().filter { it.isNotBlank() }

    private fun parseDeck(input: List<String>, player: Int) = ArrayDeque(
        input.dropWhile { it != "Player $player:" }.drop(1).takeWhile { !it.startsWith("Player") }.map { it.toInt() }
    )

    override fun solve1(input: List<String>): Int {
        val p1Deck = parseDeck(input, 1)
        val p2Deck = parseDeck(input, 2)
        while (p1Deck.isNotEmpty() && p2Deck.isNotEmpty()) {
            val p1Card = p1Deck.removeFirst()
            val p2Card = p2Deck.removeFirst()
            if (p1Card > p2Card) {
                p1Deck.addLast(p1Card)
                p1Deck.addLast(p2Card)
            } else {
                p2Deck.addLast(p2Card)
                p2Deck.addLast(p1Card)
            }
        }
        return (p1Deck.ifEmpty { p2Deck }).reversed().withIndex().sumOf { (it.index + 1) * it.value }
    }

    override fun solve2(input: List<String>): Int {
        val p1Deck = parseDeck(input, 1)
        val p2Deck = parseDeck(input, 2)
        val winnerDeck = recursiveTurn(p1Deck, p2Deck, mutableSetOf()).first
        return winnerDeck.reversed().withIndex().sumOf { (it.index + 1) * it.value }
    }

    private fun recursiveTurn(p1Deck: ArrayDeque<Int>, p2Deck: ArrayDeque<Int>, states: MutableSet<Int>): Pair<ArrayDeque<Int>, Int> {
        while (p1Deck.isNotEmpty() && p2Deck.isNotEmpty()) {
            val state = p1Deck.hashCode() * 31 + p2Deck.hashCode()
            if (!states.add(state)) return p1Deck to 1
            val p1Card = p1Deck.removeFirst()
            val p2Card = p2Deck.removeFirst()
            val p1Winner = if (p1Deck.size >= p1Card && p2Deck.size >= p2Card) {
                val res = recursiveTurn(ArrayDeque(p1Deck.take(p1Card)), ArrayDeque(p2Deck.take(p2Card)), mutableSetOf())
                res.second == 1
            } else {
                p1Card > p2Card
            }
            if (p1Winner) {
                p1Deck.addLast(p1Card)
                p1Deck.addLast(p2Card)
            } else {
                p2Deck.addLast(p2Card)
                p2Deck.addLast(p1Card)
            }
        }
        return if (p1Deck.isNotEmpty()) p1Deck to 1 else p2Deck to 2
    }
}