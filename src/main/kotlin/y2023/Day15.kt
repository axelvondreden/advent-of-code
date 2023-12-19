package y2023

import Day

class Day15 : Day<List<String>>(2023, 15) {

    override suspend fun List<String>.parse() = first().split(",")

    override suspend fun solve1(input: List<String>) = input.sumOf { it.hash() }

    override suspend fun solve2(input: List<String>): Int {
        val instructions = input.parseInstructions()
        val boxes = Array(256) { Box(it) }
        instructions.forEach { inst ->
            val id = inst.label.hash()
            if (inst.op == '-') {
                boxes[id].lenses.removeIf { it.label == inst.label }
            } else {
                val index = boxes[id].lenses.indexOfFirst { it.label == inst.label }
                if (index >= 0) {
                    boxes[id].lenses[index] = Lens(inst.focalLength!!, inst.label)
                } else {
                    boxes[id].lenses.add(Lens(inst.focalLength!!, inst.label))
                }
            }
        }
        return boxes.sumOf { box -> box.lenses.withIndex().sumOf { (box.nr + 1) * (it.index + 1) * it.value.focalLength } }
    }

    private fun List<String>.parseInstructions() = map {
        val label = it.takeWhile { it != '=' && it != '-' }
        val op = it.removePrefix(label)[0]
        val focalLength = it.substringAfter(op).toIntOrNull()
        Instruction(label, op, focalLength)
    }

    private data class Instruction(val label: String, val op: Char, val focalLength: Int?)

    private data class Box(val nr: Int, val lenses: MutableList<Lens> = mutableListOf())

    private data class Lens(val focalLength: Int, val label: String)

    private fun String.hash(): Int {
        var hash = 0
        for (c in this) {
            hash += c.code
            hash *= 17
            hash %= 256
        }
        return hash
    }
}