package y2023

import Day

class Day19 : Day<Day19.Input>(2023, 19) {

    override suspend fun List<String>.parse(): Input {
        val workflowLines = takeWhile { it.isNotBlank() }
        val partLines = takeLastWhile { it.isNotBlank() }
        return Input(
            workflows = workflowLines.map { Workflow.from(it) }.associateBy { it.name },
            ratings = partLines.map { Rating.from(it) }
        )
    }

    override suspend fun solve1(input: Input): Int = input.ratings.sumOf { it.score(input.workflows, input.workflows.getValue("in")) }

    override suspend fun solve2(input: Input): Long = input.combinations(
        result = "in",
        ranges = mapOf(
            'x' to (1..4000),
            'm' to (1..4000),
            'a' to (1..4000),
            's' to (1..4000)
        )
    )

    data class Input(val workflows: Map<String, Workflow>, val ratings: List<Rating>)

    private fun Rating.score(workflows: Map<String, Workflow>, workflow: Workflow): Int {
        val rule = workflow.rules.first { it.matches(this) }
        return when (rule.result) {
            "R"  -> 0
            "A"  -> categories.values.sum()
            else -> score(workflows, workflows.getValue(rule.result))
        }
    }

    private fun Input.combinations(result: String, ranges: Map<Char, IntRange>): Long {
        return when (result) {
            "R"  -> 0
            "A"  -> ranges.values.map { it.size().toLong() }.reduce(Long::times)
            else -> {
                val newRanges = ranges.toMutableMap()
                workflows.getValue(result).rules.sumOf { rule ->
                    when (rule) {
                        is Rule.Unconditional -> combinations(rule.result, newRanges)
                        is Rule.Conditional   -> {
                            val newRange = newRanges.getValue(rule.lhs).merge(rule.range())
                            val newReversed = newRanges.getValue(rule.lhs).merge(rule.reversedRange())

                            newRanges[rule.lhs] = newRange
                            combinations(rule.result, newRanges).also { newRanges[rule.lhs] = newReversed }
                        }
                    }
                }
            }
        }
    }

    data class Workflow(val name: String, val rules: List<Rule>) {
        companion object {
            fun from(str: String): Workflow {
                val name = str.substringBefore("{")
                val rules = str.substringAfter("{").substringBefore("}").split(",").map { Rule.from(it) }
                return Workflow(name, rules)
            }
        }
    }

    sealed class Rule {
        abstract val result: String

        data class Conditional(val lhs: Char, val op: Char, val rhs: Int, override val result: String) : Rule() {
            fun range(): IntRange = if (op == '<') (1..<rhs) else (rhs + 1..4000)
            fun reversedRange(): IntRange = if (op == '<') (rhs..4000) else (1..rhs)
        }

        data class Unconditional(override val result: String) : Rule()

        fun matches(rating: Rating): Boolean {
            return when (this) {
                is Unconditional -> true
                is Conditional   -> {
                    when (op) {
                        '>'  -> rating.categories.getValue(lhs) > rhs
                        '<'  -> rating.categories.getValue(lhs) < rhs
                        else -> error("Unsupported operation: $op")
                    }
                }
            }
        }

        companion object {
            fun from(str: String): Rule {
                return if (':' in str) {
                    val condition = str.substringBefore(":")
                    val result = str.substringAfter(":")
                    Conditional(condition[0], condition[1], condition.substring(2).toInt(), result)
                } else {
                    Unconditional(str)
                }
            }
        }
    }

    data class Rating(val categories: Map<Char, Int>) {
        companion object {
            fun from(str: String): Rating {
                val categories = str.drop(1).dropLast(1).split(",").associate {
                    it.substringBefore("=").single() to it.substringAfter("=").toInt()
                }
                return Rating(categories)
            }
        }
    }

    private fun IntRange.size() = last - start + 1

    private fun IntRange.merge(other: IntRange) = (maxOf(first, other.first)..minOf(last, other.last))
}