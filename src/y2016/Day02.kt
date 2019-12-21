package y2016

import Day
import Utils

class Day02 : Day() {

    override val input = Utils.readStrings(2016, 2)

    override fun solve1(): String {
        var code = ""
        for (input in input) {
            var nr = 5
            for (c in input) {
                nr = when (c) {
                    'U' -> if (nr > 3) nr - 3 else nr
                    'D' -> if (nr < 7) nr + 3 else nr
                    'L' -> if (nr !in intArrayOf(1, 4, 7)) nr - 1 else nr
                    'R' -> if (nr !in intArrayOf(3, 6, 9)) nr + 1 else nr
                    else -> nr
                }
            }
            code += nr
        }
        return code
    }

    override fun solve2(): String {
        var code = ""
        for (input in input) {
            var nr = 5
            for (c in input) {
                nr = when (c) {
                    'U' -> {
                        when (nr) {
                            3, 13 -> nr - 2
                            6, 7, 8, 10, 11, 12 -> nr - 4
                            else -> nr
                        }
                    }
                    'D' -> {
                        when (nr) {
                            1, 11 -> nr + 2
                            2, 3, 4, 6, 7, 8 -> nr + 4
                            else -> nr
                        }
                    }
                    'L' -> if (nr !in intArrayOf(1, 2, 5, 10, 13)) nr - 1 else nr
                    'R' -> if (nr !in intArrayOf(1, 4, 9, 12, 13)) nr + 1 else nr
                    else -> nr
                }
            }
            code += when (nr) {
                10 -> "A"
                11 -> "B"
                12 -> "C"
                13 -> "D"
                else -> nr
            }
        }
        return code
    }
}