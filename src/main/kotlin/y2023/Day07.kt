package y2023

import Day

class Day07 : Day<List<String>>(2023, 7) {

    override fun List<String>.parse() = this

    override fun solve1(input: List<String>) = solve("AKQJT98765432", input)

    override fun solve2(input: List<String>) = solve("AKQT98765432J", input)

    private fun solve(deck: String, input: List<String>): Long {
        val hands = input
            .map { line ->
                val cards = line.substringBefore(" ")
                val score = line.substringAfter(" ").toLong()
                recognize(cards, deck) to score
            }.sortedBy { it.first }

        // Smallest first
        return hands.withIndex().sumOf { (index, hand) -> (index + 1) * hand.second }
    }

    private fun recognize(cards: String, deck: String): Hand {
        val joker = deck.last() == JOKER
        val count = deck.map { card -> cards.count { (!joker && card == it) || (joker && card == it && card != JOKER) } }
            .sortedDescending()
            .toMutableList()

        // Jokers go to the strongest position.
        if (joker) count[0] += cards.count { it == JOKER }

        val one = count[0]
        val two = count[1]

        return Hand(cards, deck) {
            when {
                one == 5 -> Type.FIVE_OF_A_KIND
                one == 4 -> Type.FOUR_OF_A_KIND
                one == 3 && two == 2 -> Type.FULL_HOUSE
                one == 3 -> Type.THREE_OF_A_KIND
                one == 2 && two == 2 -> Type.TWO_PAIR
                one == 2 -> Type.ONE_PAIR
                else -> Type.HIGH_CARD
            }
        }
    }

    private enum class Type { FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD }

    private data class Hand(private val type: Type, private val deck: String, private val cards: String) : Comparable<Hand> {
        constructor(cards: String, deck: String, type: () -> Type) : this(type(), deck, cards)

        private val rank get() = Type.entries.indexOf(type)

        override fun compareTo(other: Hand) =
            if (rank - other.rank != 0) {
                other.rank - rank
            } else {
                (0..4).firstNotNullOfOrNull { index ->
                    val a = deck.indexOf(cards[index])
                    val b = deck.indexOf(other.cards[index])
                    if (a != b) b - a else null
                } ?: error("Invalid card comparison.")
            }
    }

    companion object {
        private const val JOKER = 'J'
    }
}