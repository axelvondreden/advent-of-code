package y2015

import Day


class Day10 : Day() {

    override val input = "1113222113"

    override fun solve1(): Int {
        var value = input
        repeat(40) {
            value = lookAndSay(value)
        }
        return value.length
    }

    override fun solve2(): Int {
        var value = input
        repeat(50) {
            value = lookAndSay(value)
        }
        return value.length
    }

    private fun lookAndSay(input: String): String {
        val output = StringBuilder()
        var count = 0
        var take = ' '
        for (element in input) {
            if (element == take) {
                count++
            } else {
                if (count > 0) {
                    output.append(count).append(take)
                }
                take = element
                count = 1
            }
        }
        if (count > 0) {
            output.append(count).append(take)
        }
        return output.toString()
    }
}