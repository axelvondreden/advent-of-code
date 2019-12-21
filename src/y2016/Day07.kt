package y2016

import Day
import Utils

class Day07 : Day() {

    override val input = Utils.readStrings(2016, 7)

    override fun solve1() = input.filter { it.supportsTLS() }.count()

    override fun solve2() = input.filter { it.supportsSSL() }.count()

    private fun String.supportsTLS(): Boolean {
        var tls = false
        for (i in 0 until length - 3) {
            val range = substring(i until i + 4)
            if (range[0] == range[3] && range[1] == range[2] && range[0] != range[1]) {
                for (j in i downTo 0) {
                    if (get(j) == '[') {
                        return false
                    } else if (get(j) == ']') {
                        break
                    }
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
        for (i in 0 until outer.length - 2) {
            val range = outer.substring(i until i + 3)
            if (!range.contains("-") && range[0] == range[2] && range[0] != range[1]) {
                if (inner.contains(range.substring(1) + range[1])) {
                    return true
                }
            }
        }
        return false
    }
}