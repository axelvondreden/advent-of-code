package y2015

import Day
import Utils
import hasDoubleLetter
import hasDoubleWithOffset
import hasLetterPair
import hasMinVowels
import hasNoBadWords

class Day05 : Day() {

    override val input = Utils.readStrings(2015, 5)

    override fun solve1() = input.count { it.hasMinVowels(3) && it.hasDoubleLetter() && it.hasNoBadWords() }

    override fun solve2() = input.count { it.hasLetterPair() && it.hasDoubleWithOffset() }
}