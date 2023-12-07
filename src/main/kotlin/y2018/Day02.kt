package y2018

import Day

class Day02 : Day<List<String>>(2018, 2) {

    override val input = readStrings()

    override fun solve1(input: List<String>) = input.count { id -> id.groupBy { it }.any { it.value.size == 2 } } *
            input.count { id -> id.groupBy { it }.any { it.value.size == 3 } }

    override fun solve2(input: List<String>): String {
        val similarIDs = input.findSimilarIDs()
        return similarIDs.first.indices
            .filter { similarIDs.first[it] == similarIDs.second[it] }
            .map { similarIDs.first[it] }.joinToString("")
    }

    private fun List<String>.findSimilarIDs(): Pair<String, String> {
        forEach { id1 ->
            forEach { id2 ->
                if (id1 != id2) {
                    val diffs = id1.indices.map { id1[it] == id2[it] }.count { !it }
                    if (diffs == 1) return id1 to id2
                }
            }
        }
        error("oh no!")
    }
}