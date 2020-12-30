package y2015

import Day

class Day05 : Day(2015, 5) {

    override val input = readStrings()

    override fun solve1() = input.count { it.hasMinVowels(3) && it.hasDoubleLetter() && it.hasNoBadWords() }

    override fun solve2() = input.count { it.hasLetterPair() && it.hasDoubleWithOffset() }

    private fun String.hasMinVowels(vowels: Int) = count { "aeiou".contains(it) } >= vowels

    private fun String.hasDoubleLetter() = (1 until length).any { get(it - 1) == get(it) }

    private fun String.hasNoBadWords() = arrayOf("ab", "cd", "pq", "xy").none { contains(it) }

    private fun String.hasLetterPair() = (0 until length - 2).any { substring(it + 2).contains(substring(it..it + 1)) }

    private fun String.hasDoubleWithOffset() = (0 until length - 2).any { get(it) == get(it + 2) }
}