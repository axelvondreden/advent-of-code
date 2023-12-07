package y2021

import Day

class Day10 : Day<List<String>>(2021, 10) {

    override val input = readStrings()

    private val scoreMapping = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    private val scoreMapping2 = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    override fun solve1(input: List<String>) = input.mapNotNull { it.findCorruptedChar() }.sumOf { scoreMapping[it]!! }

    override fun solve2(input: List<String>): Long {
        val scores = input.mapNotNull { it.findAutocomplete() }.map {
            it.fold(0L) { acc, c -> (acc * 5) + (scoreMapping2[c] ?: 0) }
        }
        return scores.sorted()[scores.size / 2]
    }

    private fun String.findCorruptedChar(): Char? {
        var open = ""
        forEach {
            when (it) {
                '(', '[', '{', '<' -> open += it
                else -> when {
                    open.isEmpty() -> return it
                    it == ')' && open.last() != '(' -> return it
                    it == ']' && open.last() != '[' -> return it
                    it == '}' && open.last() != '{' -> return it
                    it == '>' && open.last() != '<' -> return it
                    else -> open = open.dropLast(1)
                }
            }
        }
        return null
    }

    private fun String.findAutocomplete(): String? {
        var open = ""
        forEach {
            when (it) {
                '(', '[', '{', '<' -> open += it
                else -> when {
                    open.isEmpty() -> return null
                    it == ')' && open.last() != '(' -> return null
                    it == ']' && open.last() != '[' -> return null
                    it == '}' && open.last() != '{' -> return null
                    it == '>' && open.last() != '<' -> return null
                    else -> open = open.dropLast(1)
                }
            }
        }
        return open.reversed()
            .replace('(', ')')
            .replace('[', ']')
            .replace('{', '}')
            .replace('<', '>')
    }
}