package y2016

import Day


class Day16 : Day(2016, 16) {

    override val input = readString()

    override fun solve1(): String {
        var string = input
        while (string.length < maxLength1) {
            string = string.dragonCurve()
        }
        return string.substring(0, maxLength1).checksum()

    }

    override fun solve2(): String {
        var string = input
        while (string.length < maxLength2) {
            string = string.dragonCurve()
        }
        return string.substring(0, maxLength2).checksum()
    }

    private fun String.dragonCurve() = this + "0" + this.reversed().map { if (it == '0') '1' else '0' }.joinToString("")

    private fun String.checksum(): String {
        val checksum = StringBuilder()
        for (i in 0 until length step 2) {
            checksum.append(if (get(i) == get(i + 1)) '1' else '0')
        }
        if (checksum.length % 2 == 0) {
            return checksum.toString().checksum()
        }
        return checksum.toString()
    }

    companion object {
        const val maxLength1 = 272
        const val maxLength2 = 35651584
    }
}