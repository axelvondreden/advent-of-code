package y2015

import Day

class Day19 : Day<Day19.ReplacementInput>(2015, 19) {

    override fun List<String>.parse(): ReplacementInput {
        val replacements = takeWhile { it.isNotBlank() }.map {
            Replacement(
                it.split(" => ")[0],
                it.split(" => ")[1]
            )
        }
        return ReplacementInput(last(), replacements)
    }

    override fun solve1(input: ReplacementInput) = input.replacements.flatMap { input.start.getReplacements(it) }.distinct().size

    override fun solve2(input: ReplacementInput): Int {
        var count = 0
        var start = input.start
        while (start != "e") {
            input.replacements.forEach { (input, output) ->
                if (start.contains(output)) {
                    start = replace(start, output, input, start.lastIndexOf(output))
                    count++
                }
            }
        }
        return count
    }

    private fun replace(s: String, input: String, output: String, position: Int) =
        s.substring(0, position) + output + s.substring(position + input.length)

    private fun String.getReplacements(repl: Replacement): List<String> {
        val list = mutableListOf<String>()
        var index = indexOf(repl.input)
        while (index > -1) {
            list.add(substring(0 until index) + repl.output + substring(index + repl.input.length))
            index = indexOf(repl.input, index + 1)
        }
        return list
    }

    data class ReplacementInput(val start: String, val replacements: List<Replacement>)

    data class Replacement(val input: String, val output: String)
}