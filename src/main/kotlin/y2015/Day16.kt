package y2015

import Day

class Day16 : Day<List<String>>(2015, 16) {

    override suspend fun List<String>.parse() = map { it.removePrefix("Sue ") }

    private val searchValues = mutableMapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1
    )

    override suspend fun solve1(input: List<String>): Int {
        val sues = input.associate { s ->
            s.substringBefore(":").toInt() to s.drop(s.indexOf(":") + 2).split(", ").map {
                it.split(": ")[0] to it.split(": ")[1].toInt()
            }
        }
        sues.forEach { (key, value) ->
            var found = true
            for ((first, second) in value) {
                if (searchValues[first] != second) {
                    found = false
                    break
                }
            }
            if (found) return key
        }
        return 0
    }

    override suspend fun solve2(input: List<String>): Int {
        val sues = input.associate { s ->
            s.substringBefore(":").toInt() to s.drop(s.indexOf(":") + 2).split(", ").map {
                it.split(": ")[0] to it.split(": ")[1].toInt()
            }
        }
        sues.forEach { (key, value) ->
            var found = true
            value.forEach { (first, second) ->
                found = when (first) {
                    "cats", "trees" -> if (!found) false else searchValues[first]!! < second
                    "pomeranians", "goldfish" -> if (!found) false else searchValues[first]!! > second
                    else -> if (!found) false else searchValues[first]!! == second
                }
            }
            if (found) return key
        }
        return 0
    }
}