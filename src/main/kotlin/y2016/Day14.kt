package y2016

import Day
import md5

class Day14 : Day() {

    override val input = Utils.readString(2016, 14)

    override fun solve1(): Int {
        var index = 0
        val keys = mutableSetOf<Int>()
        while (keys.size < 64) {
            val hash = (input + index).md5()
            val letter = getTripleLetter(hash)
            if (letter != null && testForKey(index, letter)) {
                keys.add(index)
            }
            index++
        }
        return keys.maxOrNull()!!
    }

    override fun solve2(): Int {
        return 0
    }

    private fun getTripleLetter(hash: String) = (0 until hash.length - 2)
            .firstOrNull { hash[it] == hash[it + 1] && hash[it] == hash[it + 2] }
            ?.let { hash[it] }

    private fun testForKey(index: Int, char: Char): Boolean {
        val testString = char.toString().repeat(5)
        return ((index + 1)..(index + 1000))
                .map { (input + it).md5() }
                .any { it.contains(testString) }
    }
}