package y2018

import Day

class Day05 : Day<String>(2018, 5) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String) = input.fullReact().length

    override suspend fun solve2(input: String) = ('a'..'z').minOf { char ->
        input.replace(char.toString(), "", ignoreCase = true).fullReact().length
    }

    private fun String.fullReact(): String {
        var poly = this
        var newPoly = this.react()
        while (newPoly.length != poly.length) {
            poly = newPoly
            newPoly = newPoly.react()
        }
        return newPoly
    }

    private fun String.react(): String {
        val stringBuilder = StringBuilder(this)
        var index = 0
        while (index < stringBuilder.length - 1) {
            val c1 = stringBuilder[index]
            val c2 = stringBuilder[index + 1]
            if (c1.equals(c2, ignoreCase = true) && c1 != c2) {
                stringBuilder.delete(index, index + 2)
                if (index > 0) index--
            } else {
                index++
            }
        }
        return stringBuilder.toString()
    }
}