package y2023

import Day

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

    override fun solve2(input: RLMap) = input.countGhostSteps()

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

        fun countGhostSteps(): Int {
            val current = nodes.keys.filter { it.endsWith('A') }.toTypedArray()
            var count = 0
            while (!current.all { it.endsWith('Z') }) {
                val nextDir = path[count % path.length]
                current.indices.forEach {
                    current[it] = if (nextDir == 'R') {
                        nodes[current[it]]!!.second
                    } else {
                        nodes[current[it]]!!.first
                    }
                }
                count++
            }
            return count
        }
    }
}