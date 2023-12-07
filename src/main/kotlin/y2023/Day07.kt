package y2023

import Day

class Day07 : Day<List<Day07.Game>>(2023, 7) {

    override fun List<String>.parse() = map { line ->
        val s = line.split(" ")
        val cards = s[0].map { it.toCard() }
        Game(cards, s[1].toLong())
    }

    override fun solve1(input: List<Game>) = input.sorted().withIndex().sumOf { (it.index + 1) * it.value.bet }

    override fun solve2(input: List<Game>) = input.onEach { it.mode2 = true }.sorted().withIndex().sumOf { (it.index + 1) * it.value.bet }

    data class Game(val cards: List<Card>, val bet: Long) : Comparable<Game> {

        var mode2 = false

        private val cardCounts get() = if (!mode2) {
            cards.groupBy { it.value }.mapValues { it.value.size }
        } else {
            val map = cards.groupBy { it.value }.mapValues { it.value.size }.toMutableMap()
            val maxSize = map.filterKeys { it != 11 }.values.maxOrNull()
            if (maxSize != null) {
                val maxIndex = map.filterKeys { it != 11 }.filterValues { it == maxSize }.maxByOrNull { it.key }?.key
                if (maxIndex != null) {
                    map[maxIndex] = map[maxIndex]!! + (map[11] ?: 0)
                    map[11] = 0
                }
            }
            map
        }

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
                val myValue = if (mode2 && card.value == 11) 1 else card.value
                val otherValue = if (mode2 && otherCard.value == 11) 1 else otherCard.value
                if (myValue < otherValue) return -1
                if (myValue > otherValue) return 1
            }
            return 0
        }
    }

    data class Card(val value: Int)

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