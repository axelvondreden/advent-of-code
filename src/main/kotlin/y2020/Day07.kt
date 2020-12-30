package y2020

import Day

class Day07 : Day(2020, 7) {

    override val input = parseBags(readStrings())

    override fun solve1() = input.keys.count { it.canHoldColor("shiny gold") }

    override fun solve2() = "shiny gold".countNestedBags()

    private fun String.canHoldColor(color: String): Boolean {
        if (input[this]!!.contains(color)) {
            return true
        } else if (input[this]!!.any { it.key.canHoldColor(color) }) {
            return true
        }
        return false
    }

    private fun String.countNestedBags(): Int {
        var count = input[this]!!.values.sum()
        input[this]!!.forEach {
            count += it.key.countNestedBags() * it.value
        }
        return count
    }

    private fun parseBags(input: List<String>): Map<String, Map<String, Int>> {
        val map = mutableMapOf<String, Map<String, Int>>()
        input.forEach { line ->
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