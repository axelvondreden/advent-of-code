package y2020

import Day


class Day19 : Day<List<String>>(2020, 19) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val rules = input.takeWhile { it.isNotBlank() }.associate(::parseRules).toSortedMap()
        val messages = input.subList(input.indexOf("") + 1, input.size)
        return messages.count { str -> str.verify(rules, listOf(0)) }
    }

    override suspend fun solve2(input: List<String>): Int {
        val rules = input.takeWhile { it.isNotBlank() }.associate(::parseRules).toSortedMap()
        val messages = input.subList(input.indexOf("") + 1, input.size)
        rules.replace(8, Rule.Choice(listOf(listOf(42), listOf(42, 8))))
        rules.replace(11, Rule.Choice(listOf(listOf(42, 31), listOf(42, 11, 31))))
        return messages.count { str -> str.verify(rules, listOf(0)) }
    }

    private fun String.verify(rules: Map<Int, Rule>, pda: List<Int>): Boolean {
        if (pda.isEmpty()) return isEmpty()
        return when (val rule = rules[pda.first()]!!) {
            is Rule.Letter -> startsWith(rule.char) && drop(1).verify(rules, pda.drop(1))
            is Rule.Choice -> rule.choices.firstOrNull { choice -> verify(rules, choice + pda.drop(1)) } != null
        }
    }

    private fun parseRules(str: String): Pair<Int, Rule> {
        val (num, prod) = str.split(':').map(String::trim)
        if (prod.startsWith('"')) return num.toInt() to Rule.Letter(prod[1])
        return num.toInt() to Rule.Choice(
            prod.trim()
                .split('|')
                .map(String::trim)
                .map { it.split(' ').map(String::toInt) }
        )
    }

    private sealed class Rule {
        class Letter(var char: Char) : Rule()
        class Choice(var choices: List<List<Int>>) : Rule()
    }
}