package y2015

import Day

class Day11 : Day() {

    override val input = "vzbxkghb"

    private var nextPw: String? = null

    override fun solve1(): String {
        var pw = input.inc()
        while (!pw.isValidPw()) {
            pw = pw.inc()
        }
        nextPw = pw.inc()
        return pw
    }

    override fun solve2(): String {
        while (!nextPw!!.isValidPw()) {
            nextPw = nextPw!!.inc()
        }
        return nextPw!!
    }

    private fun String.inc(): String {
        var ret = ""
        var done = false
        for (c in reversed()) {
            if (!done) {
                if (c == 'z') {
                    ret += 'a'
                } else {
                    ret += c + 1
                    done = true
                }
            } else {
                ret += c
            }
        }
        return ret.reversed()
    }

    private fun String.isValidPw() = contains2Pairs() && !containsBadLetters() && has3IncreasingLetters()

    private fun String.contains2Pairs() = (1 until length).filter { get(it - 1) == get(it) }.map { get(it) }.distinct().count() >= 2

    private fun String.containsBadLetters() = contains('i') || contains('o') || contains('l')

    private fun String.has3IncreasingLetters() = (2 until length).any { get(it - 2) == get(it - 1) - 1 && get(it - 1) == get(it) - 1 }
}