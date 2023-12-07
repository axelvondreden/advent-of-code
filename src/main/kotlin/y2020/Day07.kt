package y2020

import Day

class Day07 : Day<Map<String, Map<String, Int>>>(2020, 7) {

    override fun List<String>.parse(): Map<String, Map<String, Int>> {
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

    override fun solve1(input: Map<String, Map<String, Int>>) = input.keys.count { it.canHoldColor("shiny gold", input) }

    override fun solve2(input: Map<String, Map<String, Int>>) = "shiny gold".countNestedBags(input)

    private fun String.canHoldColor(color: String, input: Map<String, Map<String, Int>>): Boolean =
        if (input[this]!!.contains(color)) true
        else input[this]!!.any { it.key.canHoldColor(color, input) }

    private fun String.countNestedBags(input: Map<String, Map<String, Int>>): Int =
        input[this]!!.values.sum() + input[this]!!.map { it.key.countNestedBags(input) * it.value }.sum()
}