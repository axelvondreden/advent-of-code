package y2015

import Day


class Day10 : Day(2015, 10) {

    override val input = readString()

    override fun solve1(): Int {
        var value = input
        repeat(40) { value = value.lookAndSay() }
        return value.length
    }

    override fun solve2(): Int {
        var value = input
        repeat(50) { value = value.lookAndSay() }
        return value.length
    }

    private fun String.lookAndSay(): String {
        val output = StringBuilder()
        var count = 0
        var take = ' '
        forEach { element ->
            if (element == take) {
                count++
            } else {
                if (count > 0) output.append(count).append(take)
                take = element
                count = 1
            }
        }
        if (count > 0) output.append(count).append(take)
        return output.toString()
    }
}