package y2021

import Day

class Day12 : Day(2021, 12) {

    override val input = readStrings()
    private val caves = input.map { it.split("-") }
        .flatMap { listOf(it.first() to it.last(), it.last() to it.first()) }
        .groupBy({ it.first }, { it.second })

    override fun solve1() = travel(cond = { n, p -> n.isUpperCase() || n !in p }).size

    override fun solve2() = travel(cond = { n, p ->
        when {
            n.isUpperCase() -> true
            n == "start" -> false
            n !in p -> true
            else -> p.filterNot { it.isUpperCase() }.groupBy { it }.none { it.value.size == 2 }
        }
    }).size

    private fun travel(cond: (String, List<String>) -> Boolean, path: List<String> = listOf("start")): List<List<String>> =
        if (path.last() == "end") listOf(path)
        else caves.getValue(path.last()).filter { cond(it, path) }.flatMap { travel(cond, path + it) }

    private fun String.isUpperCase() = all { it.isUpperCase() }
}