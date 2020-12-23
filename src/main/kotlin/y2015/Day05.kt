package y2015

import Day
import utils.IO
import utils.hasDoubleLetter
import utils.hasDoubleWithOffset
import utils.hasLetterPair
import utils.hasMinVowels
import utils.hasNoBadWords

class Day05 : Day() {

    override val input = IO.readStrings(2015, 5)

    override fun solve1() = input.count { it.hasMinVowels(3) && it.hasDoubleLetter() && it.hasNoBadWords() }

    override fun solve2() = input.count { it.hasLetterPair() && it.hasDoubleWithOffset() }
}