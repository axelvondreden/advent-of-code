package y2016

import Day
import utils.md5

class Day14 : Day<String>(2016, 14) {

    override suspend fun List<String>.parse() = first()

    private val cache = mutableMapOf<Int, String>()

    override suspend fun solve1(input: String): Int {
        cache.clear()
        var index = 0
        val keys = mutableSetOf<Int>()
        while (keys.size < 64) {
            val hash = input.getHash(index)
            val letter = getTripleLetter(hash)
            if (letter != null && testForKey(input, index, letter)) {
                keys.add(index)
            }
            index++
        }
        return keys.maxOrNull()!!
    }

    override suspend fun solve2(input: String): Int {
        cache.clear()
        var index = 0
        val keys = mutableSetOf<Int>()
        while (keys.size < 64) {
            val hash = input.getHash2016(index)
            val letter = getTripleLetter(hash)
            if (letter != null && testForKey2016(input, index, letter)) {
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

    private fun testForKey(input: String, index: Int, char: Char): Boolean {
        val testString = char.toString().repeat(5)
        return ((index + 1)..(index + 1000))
                .map { input.getHash(it) }
                .any { it.contains(testString) }
    }

    private fun testForKey2016(input: String, index: Int, char: Char): Boolean {
        val testString = char.toString().repeat(5)
        return ((index + 1)..(index + 1000))
                .map { input.getHash2016(it) }
                .any { it.contains(testString) }
    }

    private fun String.getHash(index: Int) = cache.getOrPut(index) { (this + index).md5() }

    private fun String.getHash2016(index: Int) = cache.getOrPut(index) { hash2016((this + index).md5()) }
}