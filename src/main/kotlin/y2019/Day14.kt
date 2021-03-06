package y2019

import Day

class Day14 : Day(2019, 14) {

    override val input = readStrings().map { it.split(" => ") }

    private val reactions = input.map { list ->
        val output = Pair(list[1].split(" ")[1], list[1].split(" ")[0].toLong())
        val inputs = list[0].split(", ").map { it.split(" ")[1] to it.split(" ")[0].toLong() }.toMap()
        Reaction(inputs, output)
    }

    private var oreCount = getOreCount(reactions, Pair("FUEL", 1), mutableMapOf())

    override fun solve1() = oreCount

    override fun solve2(): Long {
        var fuel = 1000000000000 / oreCount
        while (getOreCount(reactions, Pair("FUEL", fuel), emptyMap<String, Long>().toMutableMap()) < 1000000000000) {
            fuel++
        }
        return fuel - 1
    }

    private fun getOreCount(reactions: List<Reaction>, elem: Pair<String, Long>, reserves: MutableMap<String, Long>): Int =
        if (elem.first == "ORE") {
            elem.second.toInt()
        } else {
            getNeededElements(reactions, elem, reserves).entries.sumBy {
                getOreCount(reactions, Pair(it.key, it.value), reserves)
            }
        }

    private fun getNeededElements(reactions: List<Reaction>, elem: Pair<String, Long>, reserves: MutableMap<String, Long>): Map<String, Long> {
        val reaction = reactions.first { it.output.first == elem.first }
        val elems = emptyMap<String, Long>().toMutableMap()
        var multiplier = 0
        val quant = elem.second - reserves.getOrDefault(elem.first, 0)
        while (reaction.output.second * multiplier < quant) {
            multiplier++
        }
        reserves[elem.first] = reaction.output.second * multiplier - quant
        reaction.inputs.forEach { input ->
            elems[input.key] = input.value * multiplier
        }
        return elems
    }

    private data class Reaction(val inputs: Map<String, Long>, val output: Pair<String, Long>)
}