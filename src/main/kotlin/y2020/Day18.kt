package y2020

import Day


class Day18 : Day(2020, 18) {

    override val input = readStrings().map { it.replace(" ", "") }

    override fun solve1() = input.sumOf { eval1(it.iterator()) }

    override fun solve2() = input.sumOf { eval2(it.iterator()) }

    private fun eval1(equation: CharIterator): Long {
        val numbers = mutableListOf<Long>()
        var op = '+'
        while (equation.hasNext()) {
            when (val next = equation.nextChar()) {
                '(' -> numbers += eval1(equation)
                ')' -> break
                in setOf('+', '*') -> op = next
                else -> numbers += next.toString().toLong()
            }
            if (numbers.size == 2) {
                val a = numbers.removeLast()
                val b = numbers.removeLast()
                numbers += if (op == '+') a + b else a * b
            }
        }
        return numbers.first()
    }

    private fun eval2(equation: CharIterator): Long {
        val multiply = mutableListOf<Long>()
        var added = 0L
        while (equation.hasNext()) {
            val next = equation.nextChar()
            when {
                next == '(' -> added += eval2(equation)
                next == ')' -> break
                next == '*' -> {
                    multiply += added
                    added = 0L
                }
                next.isDigit() -> added += next.toString().toLong()
            }
        }
        return (multiply + added).reduce { a, b -> a * b }
    }
}