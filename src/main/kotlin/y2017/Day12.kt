package y2017

import Day
import utils.IO

class Day12 : Day() {

    override val input = parsePrograms(IO.readStrings(2017, 12))

    override fun solve1(): Int {
        val output = mutableSetOf<Program>()
        input.first { it.id == 0 }.getConnectionCount(output)
        return output.size
    }

    override fun solve2(): Int {
        val groups = mutableSetOf<Set<Program>>()
        input.forEach {
            val output = mutableSetOf<Program>()
            it.getConnectionCount(output)
            groups.add(output)
        }
        return groups.size
    }

    private fun Program.getConnectionCount(output: MutableSet<Program>) {
        if (this !in output) {
            output += this
            connections.filter { it.id != id }.forEach { it.getConnectionCount(output) }
        }
    }

    private fun parsePrograms(input: List<String>): Set<Program> {
        val programs = input.map { Program(it.split(" ")[0].toInt(), emptySet()) }.toSet()
        input.forEach { line ->
            val split = line.split(" <-> ")
            val prog = programs.first { it.id == split[0].toInt() }
            prog.connections = split[1].split(", ").map { id -> programs.first { it.id == id.toInt() } }.toSet()
        }
        return programs
    }

    data class Program(val id: Int, var connections: Set<Program>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Program
            return id != other.id
        }

        override fun hashCode() = id
    }
}
