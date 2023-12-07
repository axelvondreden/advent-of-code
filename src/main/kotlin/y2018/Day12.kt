package y2018

import Day

class Day12 : Day<List<String>>(2018, 12) {

    override val input = readStrings()

    private val initialState = input[0].removePrefix("initial state: ").mapIndexed { index, c -> index.toLong() to (c == '#') }.toMap()
    private val rules = input.subList(2, input.lastIndex + 1).mapNotNull {
        val split = it.split(" ")
        if (split[2] == "#") split[0] else null
    }

    override fun solve1(input: List<String>): Long {
        var plants = initialState
        repeat(20) {
            plants = plants.generate()
        }
        return plants.filter { it.value }.keys.sum()
    }

    override fun solve2(input: List<String>): Long {
        var plants = initialState
        repeat(100) {
            repeat(500000000) {
                plants = plants.generate()
            }
        }
        return plants.filter { it.value }.keys.sum()
    }

    private fun Map<Long, Boolean>.generate(): Map<Long, Boolean> {
        val map = mutableMapOf<Long, Boolean>()
        val min = keys.minOrNull()!!
        val max = keys.maxOrNull()!!
        (min - 2..max + 2).forEach { index ->
            val last5 = (index - 2..index + 2).map { if (getOrDefault(it, false)) '#' else '.' }.joinToString("")
            map[index] = last5 in rules
        }
        return map
    }
}