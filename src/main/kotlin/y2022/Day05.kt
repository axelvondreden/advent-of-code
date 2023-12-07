package y2022

import Day
import java.util.*

class Day05 : Day<List<String>>(2022, 5) {

    override fun List<String>.parse() = this

    override fun solve1(input: List<String>): String {
        val stackLines = input.takeWhile { it.isNotEmpty() }
        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1).map {
            val s = it.split(" ")
            Instruction(s[3].toInt(), s[5].toInt(), s[1].toInt())
        }
        val stacks = Stacks(stackLines)
        instructions.forEach { stacks.move(it) }
        return stacks.getTopChars()
    }

    override fun solve2(input: List<String>): String {
        val stackLines = input.takeWhile { it.isNotEmpty() }
        val instructions = input.dropWhile { it.isNotEmpty() }.drop(1).map {
            val s = it.split(" ")
            Instruction(s[3].toInt(), s[5].toInt(), s[1].toInt())
        }
        val stacks = Stacks(stackLines)
        instructions.forEach { stacks.move2(it) }
        return stacks.getTopChars()
    }

    private data class Instruction(val source: Int, val target: Int, val amount: Int)

    private class Stacks(lines: List<String>) {
        private val stacks: Array<Stack<Char>>

        init {
            val map = lines.last().mapIndexedNotNull { index, c -> if (c.isDigit()) c.digitToInt() to index else null }.toMap()
            stacks = Array(map.size) { Stack() }
            lines.dropLast(1).forEach { line ->
                map.forEach { (c, i) -> line.getOrNull(i)?.let { if (it.isLetter()) stacks[c - 1].add(0, it) } }
            }
        }

        fun move(inst: Instruction) {
            repeat(inst.amount) {
                stacks[inst.target - 1].push(stacks[inst.source - 1].pop())
            }
        }

        fun move2(inst: Instruction) {
            val tempStack = Stack<Char>()
            repeat(inst.amount) {
                tempStack.push(stacks[inst.source - 1].pop())
            }
            repeat(inst.amount) {
                stacks[inst.target - 1].push(tempStack.pop())
            }
        }

        fun getTopChars() = stacks.joinToString("") { it.peek().toString() }
    }
}