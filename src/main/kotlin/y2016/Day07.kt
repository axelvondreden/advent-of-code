package y2016

import Day

class Day07 : Day(2016, 7) {

    override val input = readStrings()

    override fun solve1() = input.count { it.supportsTLS() }

    override fun solve2() = input.count { it.supportsSSL() }

    private fun String.supportsTLS(): Boolean {
        var tls = false
        (0 until length - 3).forEach { i ->
            val range = substring(i until i + 4)
            if (range[0] == range[3] && range[1] == range[2] && range[0] != range[1]) {
                for (j in i downTo 0) {
                    if (get(j) == '[') return false else if (get(j) == ']') break
                }
                tls = true
            }
        }
        return tls
    }

    private fun String.supportsSSL(): Boolean {
        var outer = this
        var inner = ""
        while (outer.contains("[")) {
            val s = outer.indexOf("[") + 1
            val e = outer.indexOf("]")
            inner += outer.substring(s, e) + "-"
            outer = outer.replace(outer.substring(s - 1, e + 1), "-")
        }
        return (0 until outer.length - 2)
            .map { outer.substring(it until it + 3) }
            .any { !it.contains("-") && it[0] == it[2] && it[0] != it[1] && inner.contains(it.substring(1) + it[1]) }
    }
}