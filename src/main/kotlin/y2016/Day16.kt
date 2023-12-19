package y2016

import Day


class Day16 : Day<String>(2016, 16) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String): String {
        var string = input
        while (string.length < maxLength1) {
            string = string.dragonCurve()
        }
        return string.substring(0, maxLength1).checksum()
    }

    override suspend fun solve2(input: String): String {
        var string = input
        while (string.length < maxLength2) {
            string = string.dragonCurve()
        }
        return string.substring(0, maxLength2).checksum()
    }

    private fun String.dragonCurve() = this + "0" + this.reversed().map { if (it == '0') '1' else '0' }.joinToString("")

    private fun String.checksum(): String {
        val checksum = StringBuilder()
        (indices step 2).forEach {
            checksum.append(if (get(it) == get(it + 1)) '1' else '0')
        }
        if (checksum.length % 2 == 0) return checksum.toString().checksum()
        return checksum.toString()
    }

    private companion object {
        private const val maxLength1 = 272
        private const val maxLength2 = 35651584
    }
}