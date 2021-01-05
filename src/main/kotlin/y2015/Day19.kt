package y2015

import Day

class Day19 : Day(2015, 19) {

    override val input = readStrings().takeWhile { it.isNotBlank() }.map {
        Replacement(
            it.split(" => ")[0],
            it.split(" => ")[1]
        )
    }

    private var start = readStrings().last()

    override fun solve1() = input.flatMap { start.getReplacements(it) }.distinct().size

    override fun solve2(): Int {
        var count = 0
        while (start != "e") {
            input.forEach { (input, output) ->
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

    data class Replacement(val input: String, val output: String)
}