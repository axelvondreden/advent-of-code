package y2020

import Day


class Day16 : Day(2020, 16) {

    override val input = readStrings()

    private val rules = input.takeWhile { it.isNotBlank() }.map { line ->
        val lineSplit = line.split(": ")
        Rule(lineSplit[0], lineSplit[1].split(" or ").map {
            val split = it.split("-")
            split[0].toInt()..split[1].toInt()
        })
    }

    private val myTicket = input[input.indexOf("your ticket:") + 1].split(",").map { it.toInt() }

    private val nearbyTickets = input.subList(input.indexOf("nearby tickets:") + 1, input.size)
        .map { line -> line.split(",").map { it.toInt() } }

    override fun solve1() = nearbyTickets.flatten().filter { nr -> rules.none { it.isValid(nr) } }.sum()

    override fun solve2(): Long {
        val validTickets = nearbyTickets.filter { numbers -> numbers.all { nr -> rules.any { it.isValid(nr) } } }
        val unassignedRules = rules.toMutableSet()
        val ruleOrder = arrayOfNulls<Rule>(rules.size)
        while (null in ruleOrder) {
            for (i in ruleOrder.indices) {
                if (ruleOrder[i] == null) {
                    val possibleRules = mutableSetOf<Rule>()
                    for (rule in unassignedRules) {
                        if (validTickets.all { rule.isValid(it[i]) }) {
                            possibleRules.add(rule)
                        }
                    }
                    if (possibleRules.size == 1) {
                        ruleOrder[i] = possibleRules.first()
                        unassignedRules.remove(possibleRules.first())
                        break
                    }
                }
            }
        }
        val departureIndices = ruleOrder.mapIndexed { index, rule -> if (rule!!.name.startsWith("departure")) index else null }.filterNotNull()
        var product = 1L
        departureIndices.forEach {
            product *= myTicket[it]
        }
        return product
    }

    data class Rule(val name: String, private val ranges: List<IntRange>) {
        fun isValid(nr: Int) = ranges.any { nr in it }
    }
}