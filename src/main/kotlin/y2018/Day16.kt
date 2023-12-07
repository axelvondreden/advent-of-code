package y2018

import Day

class Day16 : Day<List<String>>(2018, 16) {

    override val input = readStrings().filter { it.isNotBlank() }

    private val samples: List<Sample>
    private val ops: List<IntArray>

    init {
        val list = mutableListOf<Sample>()
        var index = 0
        while (input[index].startsWith("Before")) {
            val before = input[index].drop(9).dropLast(1).split(", ").map { it.toInt() }.toIntArray()
            val op = input[index + 1].split(" ").map { it.toInt() }.toIntArray()
            val after = input[index + 2].drop(9).dropLast(1).split(", ").map { it.toInt() }.toIntArray()
            list += Sample(before, op, after)
            index += 3
        }
        samples = list
        ops = input.subList(index, input.size).map { it.split(" ").map { it.toInt() }.toIntArray() }
    }

    override fun solve1(input: List<String>) = samples.count { it.countSuccessfulOps() >= 3 }

    override fun solve2(input: List<String>): Int {
        return 0
    }

    private class Sample(val before: IntArray, val op: IntArray, val after: IntArray) {
        fun countSuccessfulOps(): Int {
            var count = 0
            val a = op[1]
            val b = op[2]
            val c = op[3]
            if (before.copyOf().addr(a, b, c).contentEquals(after)) count++
            if (before.copyOf().addi(a, b, c).contentEquals(after)) count++
            if (before.copyOf().mulr(a, b, c).contentEquals(after)) count++
            if (before.copyOf().muli(a, b, c).contentEquals(after)) count++
            if (before.copyOf().banr(a, b, c).contentEquals(after)) count++
            if (before.copyOf().bani(a, b, c).contentEquals(after)) count++
            if (before.copyOf().borr(a, b, c).contentEquals(after)) count++
            if (before.copyOf().bori(a, b, c).contentEquals(after)) count++
            if (before.copyOf().setr(a, b, c).contentEquals(after)) count++
            if (before.copyOf().seti(a, b, c).contentEquals(after)) count++
            if (before.copyOf().gtir(a, b, c).contentEquals(after)) count++
            if (before.copyOf().gtri(a, b, c).contentEquals(after)) count++
            if (before.copyOf().gtrr(a, b, c).contentEquals(after)) count++
            if (before.copyOf().eqir(a, b, c).contentEquals(after)) count++
            if (before.copyOf().eqri(a, b, c).contentEquals(after)) count++
            if (before.copyOf().eqrr(a, b, c).contentEquals(after)) count++
            return count
        }
    }

    companion object {
        fun IntArray.addr(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] + this[b] }
        fun IntArray.addi(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] + b }
        fun IntArray.mulr(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] * this[b] }
        fun IntArray.muli(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] * b }
        fun IntArray.banr(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] and this[b] }
        fun IntArray.bani(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] and b }
        fun IntArray.borr(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] or this[b] }
        fun IntArray.bori(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] or b }
        fun IntArray.setr(a: Int, b: Int, c: Int) = this.apply { this[c] = this[a] }
        fun IntArray.seti(a: Int, b: Int, c: Int) = this.apply { this[c] = a }
        fun IntArray.gtir(a: Int, b: Int, c: Int) = this.apply { this[c] = if (a > this[b]) 1 else 0 }
        fun IntArray.gtri(a: Int, b: Int, c: Int) = this.apply { this[c] = if (this[a] > b) 1 else 0 }
        fun IntArray.gtrr(a: Int, b: Int, c: Int) = this.apply { this[c] = if (this[a] > this[b]) 1 else 0 }
        fun IntArray.eqir(a: Int, b: Int, c: Int) = this.apply { this[c] = if (a == this[b]) 1 else 0 }
        fun IntArray.eqri(a: Int, b: Int, c: Int) = this.apply { this[c] = if (this[a] == b) 1 else 0 }
        fun IntArray.eqrr(a: Int, b: Int, c: Int) = this.apply { this[c] = if (this[a] == this[b]) 1 else 0 }
    }
}