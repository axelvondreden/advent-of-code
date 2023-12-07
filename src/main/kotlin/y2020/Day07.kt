package y2020

import Day

class Day07 : Day<List<String>>(2020, 7) {

    override val input = readStrings().toBags()

    override fun solve1(input: List<String>) = input.keys.count { it.canHoldColor("shiny gold") }

    override fun solve2(input: List<String>) = "shiny gold".countNestedBags()

    private fun String.canHoldColor(color: String): Boolean =
        if (input[this]!!.contains(color)) true
        else input[this]!!.any { it.key.canHoldColor(color) }

    private fun String.countNestedBags(): Int =
        input[this]!!.values.sum() + input[this]!!.map { it.key.countNestedBags() * it.value }.sum()

    private fun List<String>.toBags(): Map<String, Map<String, Int>> {
        val map = mutableMapOf<String, Map<String, Int>>()
        forEach { line ->
            val split = line.split(" bags contain ")
            val childrenSplit = split[1].split(", ")
            val childrenList = mutableMapOf<String, Int>()
            if (childrenSplit[0].first().isDigit()) {
                childrenSplit.forEach {
                    val childSplit = it.split(" ")
                    childrenList[childSplit[1] + " " + childSplit[2]] = childSplit[0].toInt()
                }
            }
            map[split[0]] = childrenList
        }
        return map
    }
}