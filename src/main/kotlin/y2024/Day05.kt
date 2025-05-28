package y2024

import Day
import utils.set

class Day05 : Day<Pair<List<Day05.Rule>, List<List<Int>>>>(2024, 5) {

    data class Rule(val first: Int, val second: Int)

    override suspend fun List<String>.parse(): Pair<List<Rule>, List<List<Int>>> {
        val rules = mutableListOf<Rule>()
        val pages = mutableListOf<List<Int>>()
        forEach { line ->
            if (!line.isNullOrBlank()) {
                if ('|' in line) {
                    rules += line.split("|").let { Rule(it[0].toInt(), it[1].toInt()) }
                } else {
                    pages += line.split(",").map { it.toInt() }
                }
            }
        }
        return rules to pages
    }

    override suspend fun solve1(input: Pair<List<Rule>, List<List<Int>>>) =
        input.second.filter { it.isValid(input.first) }.sumOf { page -> page[page.size / 2] }

    override suspend fun solve2(input: Pair<List<Rule>, List<List<Int>>>) =
        input.second.filter { !it.isValid(input.first) }
            .map { page ->
                val newPage = page.toMutableList()
                while (!newPage.isValid(input.first)) {
                    val rule = input.first.first { !newPage.checkRule(it) }
                    val index1 = newPage.indexOf(rule.first)
                    val index2 = newPage.indexOf(rule.second)
                    newPage[index1] = rule.second
                    newPage[index2] = rule.first
                }
                newPage
            }
            .sumOf { page -> page[page.size / 2] }

    private fun List<Int>.isValid(rules: List<Rule>): Boolean {
        return rules.all { checkRule(it) }
    }

    private fun List<Int>.checkRule(rule: Rule): Boolean {
        if (rule.first !in this || rule.second !in this) return true
        return (indexOf(rule.first) < indexOf(rule.second))
    }
}