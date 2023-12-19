package y2018

import Day

class Day05 : Day<String>(2018, 5) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String) = input.fullReact().length

    override suspend fun solve2(input: String) = ('a'..'z').minOf { input.replace(it.toString(), "", ignoreCase = true).fullReact().length }

    private fun String.fullReact(): String {
        var poly = this
        var newPoly = poly.react()
        while (newPoly.length != poly.length) {
            poly = newPoly
            newPoly = poly.react()
        }
        return newPoly
    }

    private fun String.react(): String {
        val index = (0 until length - 1).firstOrNull {
            val c1 = get(it)
            val c2 = get(it + 1)
            c1.equals(c2, ignoreCase = true) && c1 != c2
        }
        return index?.let { removeRange(it, index + 2) } ?: this
    }
}