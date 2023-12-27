package y2018

import Day

class Day14 : Day<Int>(2018, 14) {
    override suspend fun List<String>.parse() = first().toInt()

    override suspend fun solve1(input: Int) = recipes { it.size == input + 10 }.takeLast(10).joinToString("")

    override suspend fun solve2(input: Int) = recipes { it.endsWith(input.asDigits()) }.size - input.asDigits().size

    private fun recipes(stopCondition: (List<Int>) -> Boolean): List<Int> {
        val history = mutableListOf(3, 7)
        var elf1 = 0
        var elf2 = 1
        var stop = false

        while (!stop) {
            val nextValue = history[elf1] + history[elf2]
            nextValue.asDigits().forEach {
                if (!stop) {
                    history.add(it)
                    stop = stopCondition(history)
                }
            }
            elf1 = (elf1 + history[elf1] + 1) % history.size
            elf2 = (elf2 + history[elf2] + 1) % history.size
        }
        return history
    }

    private fun Int.asDigits() = toString().map { it.toString().toInt() }

    private fun List<Int>.endsWith(other: List<Int>): Boolean =
        if (this.size < other.size) false
        else this.slice(this.size - other.size until this.size) == other
}