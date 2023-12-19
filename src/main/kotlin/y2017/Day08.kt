package y2017

import Day
import kotlin.math.max

class Day08 : Day<List<Day08.Instruction>>(2017, 8) {

    override suspend fun List<String>.parse() = map {
        val split = it.split(" ")
        Instruction(split[0], split[1], split[2].toInt(), Condition(split[4], split[5].toOp(), split[6].toInt()))
    }

    override suspend fun solve1(input: List<Instruction>): Int {
        val register = mutableMapOf<String, Int>()
        input.forEach { if (it.condition.verify(register)) it.execute(register) }
        return register.values.maxOrNull()!!
    }

    override suspend fun solve2(input: List<Instruction>): Int {
        val register = mutableMapOf<String, Int>()
        var max = 0
        input.forEach {
            if (it.condition.verify(register)) {
                it.execute(register)
                max = max(max, register.values.maxOrNull()!!)
            }
        }
        return max
    }

    private fun String.toOp() = when (this) {
        "==" -> Op.EQ
        "!=" -> Op.NEQ
        ">" -> Op.GT
        ">=" -> Op.GTE
        "<" -> Op.LT
        "<=" -> Op.LTE
        else -> throw RuntimeException("oh no!")
    }

    data class Instruction(val target: String, val op: String, val arg: Int, val condition: Condition) {
        fun execute(register: MutableMap<String, Int>) {
            when (op) {
                "inc" -> register[target] = register.getOrPut(target) { 0 } + arg
                "dec" -> register[target] = register.getOrPut(target) { 0 } - arg
            }
        }
    }

    data class Condition(val arg1: String, val op: Op, val arg2: Int) {
        fun verify(register: MutableMap<String, Int>): Boolean {
            return when (op) {
                Op.EQ -> register.getOrPut(arg1) { 0 } == arg2
                Op.NEQ -> register.getOrPut(arg1) { 0 } != arg2
                Op.GT -> register.getOrPut(arg1) { 0 } > arg2
                Op.GTE -> register.getOrPut(arg1) { 0 } >= arg2
                Op.LT -> register.getOrPut(arg1) { 0 } < arg2
                Op.LTE -> register.getOrPut(arg1) { 0 } <= arg2
            }
        }
    }

    enum class Op {
        EQ, NEQ, GT, GTE, LT, LTE
    }
}
