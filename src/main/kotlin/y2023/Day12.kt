package y2023

import Day

class Day12 : Day<List<Day12.Row>>(2023, 12) {

    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()

    override suspend fun List<String>.parse() = map { line -> line.split(" ").let { Row(it[0], it[1]) } }

    override suspend fun solve1(input: List<Row>) = input.sumOf { row ->
        count(row.springs, row.groups.split(",").map { it.toInt() })
    }

    override suspend fun solve2(input: List<Row>) = input.sumOf { row ->
        count("${row.springs}?".repeat(5).dropLast(1), "${row.groups},".repeat(5).dropLast(1).split(",").map { it.toInt() })
    }

    private fun count(config: String, groups: List<Int>): Long {
        if (groups.isEmpty()) return if ("#" in config) 0 else 1
        if (config.isEmpty()) return 0

        return cache.getOrPut(config to groups) {
            var result = 0L
            if (config.first() in ".?")
                result += count(config.drop(1), groups)
            if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
                result += count(config.drop(groups.first() + 1), groups.drop(1))
            result
        }
    }

    data class Row(val springs: String, val groups: String)
}
