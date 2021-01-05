package y2020

import Day


class Day23 : Day(2020, 23) {

    override val input = readString()

    override fun solve1() = Cups(input).allMoves(100).toString()

    override fun solve2() = Cups(input, 1000000)
        .allMoves(10000000)
        .nextAsList(2)
        .map { it.value.toLong() }
        .reduce { a, b -> a * b }

    private class Cups(list: String, amount: Int = list.length) {
        val cups = List(amount + 1) { Cup(it) }
        var current = cups[list.first().toString().toInt()]

        init {
            val orderedCups = list.map { it.toString().toInt() }.plus((list.length + 1..amount))
            orderedCups.map { cups[it] }.fold(cups[list.last().toString().toInt()]) { prev, cup ->
                cup.also { prev.next = cup }
            }
            cups[orderedCups.last()].next = cups[orderedCups.first()]
        }

        fun allMoves(rounds: Int): Cup {
            repeat(rounds) { playRound() }
            return cups[1]
        }

        private fun playRound() {
            val next3 = current.nextAsList(3)
            val destination = calculateDestination(next3.map { it.value }.toSet())
            moveCups(next3, destination)
            current = current.next
        }

        private fun moveCups(cupsToInsert: List<Cup>, destination: Cup) {
            val prevDest = destination.next
            current.next = cupsToInsert.last().next
            destination.next = cupsToInsert.first()
            cupsToInsert.last().next = prevDest
        }

        private fun calculateDestination(exempt: Set<Int>): Cup {
            var dest = current.value - 1
            while (dest in exempt || dest == 0) {
                dest = if (dest == 0) cups.size - 1 else dest - 1
            }
            return cups[dest]
        }
    }

    private class Cup(val value: Int) {

        lateinit var next: Cup

        fun nextAsList(n: Int) = (1..n).runningFold(this) { cur, _ -> cur.next }.drop(1)

        override fun toString() = buildString {
            var current = this@Cup.next
            while (current != this@Cup) {
                append(current.value.toString())
                current = current.next
            }
        }
    }
}