package y2020

import Day

class Day06 : Day<List<String>>(2020, 6) {

    override fun List<String>.parse() = this.toGroups()

    override fun solve1(input: List<String>) = input.sumOf { it.yesCount }

    override fun solve2(input: List<String>) = input.sumOf { it.allYesCount }

    private fun List<String>.toGroups(): List<Group> {
        val groups = mutableListOf<Group>()
        val answers = mutableListOf<String>()
        forEach {
            if (it.isBlank()) {
                groups.add(Group(answers.toList()))
                answers.clear()
            } else {
                answers.add(it)
            }
        }
        groups.add(Group(answers))
        return groups
    }

    data class Group(val answers: List<String>) {
        val yesCount get() = answers.joinToString("").chars().distinct().count().toInt()
        val allYesCount get() = ('a'..'z').count { char -> answers.all { it.contains(char) } }
    }
}