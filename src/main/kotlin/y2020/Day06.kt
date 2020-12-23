package y2020

import Day
import utils.IO

class Day06 : Day() {

    override val input = parseGroups(IO.readStrings(2020, 6))

    override fun solve1() = input.sumBy { it.yesCount }

    override fun solve2() = input.sumBy { it.allYesCount }

    private fun parseGroups(input: List<String>): List<Group> {
        val groups = mutableListOf<Group>()
        val answers = mutableListOf<String>()
        for (it in input) {
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