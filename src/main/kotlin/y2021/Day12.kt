package y2021

import Day

class Day12 : Day<List<String>>(2021, 12) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val caves = input.map { it.split("-") }
            .flatMap { listOf(it.first() to it.last(), it.last() to it.first()) }
            .groupBy({ it.first }, { it.second })
        return caves.travel(cond = { n, p -> n.isUpperCase() || n !in p }).size
    }

    override suspend fun solve2(input: List<String>): Int {
        val caves = input.map { it.split("-") }
            .flatMap { listOf(it.first() to it.last(), it.last() to it.first()) }
            .groupBy({ it.first }, { it.second })
        return caves.travel(cond = { n, p ->
            when {
                n.isUpperCase() -> true
                n == "start" -> false
                n !in p -> true
                else -> p.filterNot { it.isUpperCase() }.groupBy { it }.none { it.value.size == 2 }
            }
        }).size
    }

    private fun Map<String, List<String>>.travel(cond: (String, List<String>) -> Boolean, path: List<String> = listOf("start")): List<List<String>> =
        if (path.last() == "end") listOf(path)
        else getValue(path.last()).filter { cond(it, path) }.flatMap { travel(cond, path + it) }

    private fun String.isUpperCase() = all { it.isUpperCase() }
}