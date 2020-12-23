package y2016

import Day
import utils.IO
import utils.md5

class Day14 : Day() {

    override val input = IO.readString(2016, 14)

    private val cache = mutableMapOf<Int, String>()

    override fun solve1(): Int {
        cache.clear()
        var index = 0
        val keys = mutableSetOf<Int>()
        while (keys.size < 64) {
            val hash = getHash(index)
            val letter = getTripleLetter(hash)
            if (letter != null && testForKey(index, letter)) {
                keys.add(index)
            }
            index++
        }
        return keys.maxOrNull()!!
    }

    override fun solve2(): Int {
        cache.clear()
        var index = 0
        val keys = mutableSetOf<Int>()
        while (keys.size < 64) {
            val hash = getHash2016(index)
            val letter = getTripleLetter(hash)
            if (letter != null && testForKey2016(index, letter)) {
                keys.add(index)
            }
            index++
        }
        return keys.maxOrNull()!!
    }

    private fun hash2016(input: String): String {
        var hash = input
        repeat(2016) {
            hash = hash.md5()
        }
        return hash
    }

    private fun getTripleLetter(hash: String) = (0 until hash.length - 2)
            .firstOrNull { hash[it] == hash[it + 1] && hash[it] == hash[it + 2] }
            ?.let { hash[it] }

    private fun testForKey(index: Int, char: Char): Boolean {
        val testString = char.toString().repeat(5)
        return ((index + 1)..(index + 1000))
                .map { getHash(it) }
                .any { it.contains(testString) }
    }

    private fun testForKey2016(index: Int, char: Char): Boolean {
        val testString = char.toString().repeat(5)
        return ((index + 1)..(index + 1000))
                .map { getHash2016(it) }
                .any { it.contains(testString) }
    }

    private fun getHash(index: Int) = cache.getOrPut(index) { (input + index).md5() }

    private fun getHash2016(index: Int) = cache.getOrPut(index) { hash2016((input + index).md5()) }
}