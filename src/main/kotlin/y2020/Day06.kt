package y2020

import Day

class Day06 : Day(2020, 6) {

    override val input = readStrings().toGroups()

    override fun solve1() = input.sumBy { it.yesCount }

    override fun solve2() = input.sumBy { it.allYesCount }

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