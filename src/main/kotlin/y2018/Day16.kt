package y2018

import Day

class Day16 : Day<List<String>>(2018, 16) {

    override fun List<String>.parse() = filter { it.isNotBlank() }

    override fun solve1(input: List<String>): Int {
        val samples = mutableListOf<Sample>()
        var index = 0
        while (input[index].startsWith("Before")) {
            val before = input[index].drop(9).dropLast(1).split(", ").map { it.toInt() }.toIntArray()
            val op = input[index + 1].split(" ").map { it.toInt() }.toIntArray()
            val after = input[index + 2].drop(9).dropLast(1).split(", ").map { it.toInt() }.toIntArray()
            samples += Sample(before, op, after)
            index += 3
        }
        return samples.count { it.getSuccessfulOps().size >= 3 }
    }

    override fun solve2(input: List<String>): Int {
        val samples = mutableListOf<Sample>()
        var index = 0
        while (input[index].startsWith("Before")) {
            val before = input[index].drop(9).dropLast(1).split(", ").map { it.toInt() }.toIntArray()
            val op = input[index + 1].split(" ").map { it.toInt() }.toIntArray()
            val after = input[index + 2].drop(9).dropLast(1).split(", ").map { it.toInt() }.toIntArray()
            samples += Sample(before, op, after)
            index += 3
        }
        val samplesWithOps = samples.associateWith { it.getSuccessfulOps().toMutableSet() }
        val opCodes = samples.map { it.op.first() }.distinct().associateWith<Int, Op?> { null }.toMutableMap()
        while (opCodes.values.any { it == null }) {
            for ((sample, ops) in samplesWithOps) {
                if (ops.size == 1) {
                    val op = ops.first()
                    opCodes[sample.op.first()] = op
                    samplesWithOps.values.forEach { it.remove(op) }
                }
            }
        }

        val program = input.subList(index, input.size).map { line -> line.split(" ").map { it.toInt() }.toIntArray() }
        val register = IntArray(4) { 0 }
        program.forEach { line ->
            val op = opCodes[line[0]]!!
            with(op) { register.run(line[1], line[2], line[3]) }
        }
        return register[0]
    }

    private class Sample(val before: IntArray, val op: IntArray, val after: IntArray) {
        fun getSuccessfulOps(): Set<Op> {
            val a = op[1]
            val b = op[2]
            val c = op[3]
            return allOps.filter {
                with(it) {
                    val copy = before.copyOf()
                    copy.run(a, b, c)
                    copy.contentEquals(after)
                }
            }.toSet()
        }
    }

    sealed class Op(val run: IntArray.(Int, Int, Int) -> Unit) {
        data object Addr : Op({ a, b, c -> this[c] = this[a] + this[b] })
        data object Addi : Op({ a, b, c -> this[c] = this[a] + b })
        data object Mulr : Op({ a, b, c -> this[c] = this[a] * this[b] })
        data object Muli : Op({ a, b, c -> this[c] = this[a] * b })
        data object Banr : Op({ a, b, c -> this[c] = this[a] and this[b] })
        data object Bani : Op({ a, b, c -> this[c] = this[a] and b })
        data object Borr : Op({ a, b, c -> this[c] = this[a] or this[b] })
        data object Bori : Op({ a, b, c -> this[c] = this[a] or b })
        data object Setr : Op({ a, _, c -> this[c] = this[a] })
        data object Seti : Op({ a, _, c -> this[c] = a })
        data object Gtir : Op({ a, b, c -> this[c] = if (a > this[b]) 1 else 0 })
        data object Gtri : Op({ a, b, c -> this[c] = if (this[a] > b) 1 else 0 })
        data object Gtrr : Op({ a, b, c -> this[c] = if (this[a] > this[b]) 1 else 0 })
        data object Eqir : Op({ a, b, c -> this[c] = if (a == this[b]) 1 else 0 })
        data object Eqri : Op({ a, b, c -> this[c] = if (this[a] == b) 1 else 0 })
        data object Eqrr : Op({ a, b, c -> this[c] = if (this[a] == this[b]) 1 else 0 })
    }

    companion object {
        private val allOps = setOf(
            Op.Addr,
            Op.Addi,
            Op.Mulr,
            Op.Muli,
            Op.Banr,
            Op.Bani,
            Op.Borr,
            Op.Bori,
            Op.Setr,
            Op.Seti,
            Op.Gtir,
            Op.Gtri,
            Op.Gtrr,
            Op.Eqir,
            Op.Eqri,
            Op.Eqrr
        )
    }
}