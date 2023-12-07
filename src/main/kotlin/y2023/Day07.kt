package y2023

import Day

class Day07 : Day<List<String>>(2023, 7) {

    override fun List<String>.parse() = this

    private val games = input.map { line ->
        val s = line.split(" ")
        val cards = s[0].map { it.toCard() }
        Game(cards, s[1].toLong())
    }

    override fun solve1(input: List<String>) = games.sorted().withIndex().sumOf { (it.index + 1) * it.value.bet }

    override fun solve2(input: List<String>) = 0

    private data class Game(val cards: List<Card>, val bet: Long) : Comparable<Game> {

        private val cardCounts = cards.groupBy { it.value }.mapValues { it.value.size }

        private fun has5OfAKind() = cardCounts.size == 1

        private fun has4OfAKind() = cardCounts.any { it.value == 4 }

        private fun hasFullHouse() = cardCounts.size == 2 && !has4OfAKind()

        private fun has3OfAKind() = cardCounts.size == 3 && cardCounts.any { it.value == 3 }

        private fun hasTwoPairs() = cardCounts.size == 3 && cardCounts.count { it.value == 2 } == 2

        private fun hasPair() = cardCounts.size == 4

        private fun getTypeScore() = when {
            has5OfAKind() -> 7
            has4OfAKind() -> 6
            hasFullHouse() -> 5
            has3OfAKind() -> 4
            hasTwoPairs() -> 3
            hasPair() -> 2
            else -> 1
        }

        override fun compareTo(other: Game): Int {
            val myScore = getTypeScore()
            val otherScore = other.getTypeScore()
            if (myScore < otherScore) return -1
            if (myScore > otherScore) return 1
            cards.forEachIndexed { index, card ->
                val otherCard = other.cards[index]
                if (card.value < otherCard.value) return -1
                if (card.value > otherCard.value) return 1
            }
            return 0
        }
    }

    private data class Card(val value: Int)

    private fun Char.toCard() = Card(
        when (this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> digitToInt()
        }
    )
}