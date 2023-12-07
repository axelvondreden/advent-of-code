package y2017

import Day

class Day12 : Day<Any?>(2017, 12) {

    override val input = readStrings().toPrograms()

    override fun solve1(input: List<String>): Int {
        val output = mutableSetOf<Program>()
        input.first { it.id == 0 }.getConnectionCount(output)
        return output.size
    }

    override fun solve2(input: List<String>): Int {
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

    private fun List<String>.toPrograms(): Set<Program> {
        val programs = map { Program(it.split(" ")[0].toInt(), emptySet()) }.toSet()
        forEach { line ->
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
