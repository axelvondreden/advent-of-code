package y2023

import Day
import java.math.BigInteger



class Day08 : Day<Day08.RLMap>(2023, 8) {

    override fun List<String>.parse(): RLMap {
        val path = first()
        val nodes = drop(2).associate {
            val s = it.split(" = ")
            val s2 = s[1].drop(1).dropLast(1).split(", ")
            s[0] to (s2[0] to s2[1])
        }
        return RLMap(path, nodes)
    }

    override fun solve1(input: RLMap) = input.countSteps()

    override fun solve2(input: RLMap): BigInteger = input.countGhostSteps()

    data class RLMap(val path: String, val nodes: Map<String, Pair<String, String>>) {

        fun countSteps(): Int {
            var current = "AAA"
            var count = 0
            while (current != "ZZZ") {
                val nextDir = path[count % path.length]
                current = if (nextDir == 'R') {
                    nodes[current]!!.second
                } else {
                    nodes[current]!!.first
                }
                count++
            }
            return count
        }

        fun countGhostSteps(): BigInteger = nodes.keys.filter { it.endsWith('A') }.stream()
            .map { node ->
                var current = node
                var steps = 0
                while (!current.endsWith('Z')) {
                    current = if ((path[steps++ % path.length] == 'L')) nodes[current]!!.first else nodes[current]!!.second
                }
                BigInteger.valueOf(steps.toLong())
            }.reduce(BigInteger.ONE) { a, b ->
                val gcd = a.gcd(b)
                val absProduct = a.multiply(b).abs()
                absProduct.divide(gcd)
            }
    }
}