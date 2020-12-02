package y2016

import Day
import Utils


class Day16 : Day() {

    override val input = Utils.readString(2016, 16)

    override fun solve1(): String {
        var string = input
        while (string.length < maxLength) {
            string = string.dragonCurve()
        }
        return string.substring(0, maxLength).checksum()

    }

    override fun solve2() = 0

    private fun String.dragonCurve() = this + "0" + this.reversed().map { if (it == '0') '1' else '0' }.joinToString("")

    private fun String.checksum(): String {
        var checksum = ""
        for (i in 0 until length step 2) {
            checksum += if (get(i) == get(i + 1)) '1' else '0'
        }
        if (checksum.length % 2 == 0) {
            return checksum.checksum()
        }
        return checksum
    }

    companion object {
        const val maxLength = 272
    }
}