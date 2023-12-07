package y2017

import Day
import kotlin.math.absoluteValue

class Day07 : Day<Any?>(2017, 7) {

    override val input = readStrings().toMainProgram()

    override fun solve1(input: List<String>) = input.name

    override fun solve2(input: List<String>) = input.findWrongWeight()

    private fun List<String>.toMainProgram(): Program {
        val left = toMutableList()
        val parsed = mutableListOf<Program>()
        var index = 0
        while (left.size > 0) {
            val line = left[index]
            if (!line.contains("->")) {
                parsed.add(Program(line.split(" ")[0], line.split(" ")[1].drop(1).dropLast(1).toInt(), emptyList()))
                left.removeAt(index)
                index = 0
            } else {
                val children = line.split(" -> ")[1].split(", ")
                if (children.all { child -> parsed.any { it.name == child } }) {
                    parsed.add(
                        Program(
                            line.split(" ")[0],
                            line.split(" ")[1].drop(1).dropLast(1).toInt(),
                            children.map { child -> parsed.first { it.name == child } }
                        )
                    )
                    left.removeAt(index)
                    index = 0
                } else {
                    index++
                    if (index >= left.size) index = 0
                }
            }
        }
        return parsed.last()
    }

    data class Program(val name: String, val weight: Int, val children: List<Program>) {

        fun findWrongWeight(diff: Int? = null): Int =
            if (diff != null && isBalanced) {
                weight - diff
            } else {
                val groupedChildren = children.groupBy { it.combinedWeight }
                groupedChildren.minByOrNull { it.value.size }!!.value.first()
                    .findWrongWeight(diff ?: groupedChildren.keys.reduce { a, b -> a - b }.absoluteValue)
            }

        private val combinedWeight: Int by lazy { weight + children.sumOf { it.combinedWeight } }

        private val isBalanced: Boolean by lazy { children.map { it.combinedWeight }.distinct().size == 1 }
    }
}
