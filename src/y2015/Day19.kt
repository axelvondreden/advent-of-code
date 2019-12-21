package y2015

import Day
import Utils

class Day19 : Day() {

    override val input = Utils.readStrings(2015, 19).map {
        Replacement(
            it.split(" => ")[0],
            it.split(" => ")[1]
        )
    }

    private var start = "CRnCaCaCaSiRnBPTiMgArSiRnSiRnMgArSiRnCaFArTiTiBSiThFYCaFArCaCaSiThCaPBSiThSiThCaCaPTiRnPBSiThRnFArArCaCaSiThCaSiThSiRnMgArCaPTiBPRnFArSiThCaSiRnFArBCaSiRnCaPRnFArPMgYCaFArCaPTiTiTiBPBSiThCaPTiBPBSiRnFArBPBSiRnCaFArBPRnSiRnFArRnSiRnBFArCaFArCaCaCaSiThSiThCaCaPBPTiTiRnFArCaPTiBSiAlArPBCaCaCaCaCaSiRnMgArCaSiThFArThCaSiThCaSiRnCaFYCaSiRnFYFArFArCaSiRnFYFArCaSiRnBPMgArSiThPRnFArCaSiRnFArTiRnSiRnFYFArCaSiRnBFArCaSiRnTiMgArSiThCaSiThCaFArPRnFArSiRnFArTiTiTiTiBCaCaSiRnCaCaFYFArSiThCaPTiBPTiBCaSiThSiRnMgArCaF"

    override fun solve1() = input.flatMap { getReplacements(start, it) }.distinct().size

    override fun solve2(): Int {
        var count = 0
        while (start != "e") {
            for ((input, output) in input) {
                if (start.contains(output)) {
                    start = replace(start, output, input, start.lastIndexOf(output))
                    count++
                }
            }
        }
        return count
    }

    private fun replace(s: String, input: String, output: String, position: Int) = s.substring(0, position) + output + s.substring(position + input.length)

    private fun getReplacements(start: String, repl: Replacement): List<String> {
        val list = mutableListOf<String>()
        var index = start.indexOf(repl.input)
        while (index > -1) {
            list.add(start.substring(0 until index) + repl.output + start.substring(index + repl.input.length))
            index = start.indexOf(repl.input, index + 1)
        }
        return list
    }

    data class Replacement(val input: String, val output: String)
}