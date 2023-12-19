package y2020

import Day
import kotlin.math.pow


class Day14 : Day<List<String>>(2020, 14) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Long {
        var currentMask = ""
        val memory = mutableMapOf<Int, Long>()
        input.forEach {
            val split = it.split(" = ")
            if (split[0] == "mask") {
                currentMask = split[1]
            } else {
                val adress = split[0].drop(4).dropLast(1).toInt()
                val value = split[1].toLong().applyMask(currentMask)
                memory[adress] = value
            }
        }
        return memory.values.sum()
    }

    override suspend fun solve2(input: List<String>): Long {
        var currentMask = ""
        val memory = mutableMapOf<Long, Long>()
        input.forEach {
            val split = it.split(" = ")
            if (split[0] == "mask") {
                currentMask = split[1]
            } else {
                val adresses = split[0].drop(4).dropLast(1).toInt().applyMask2(currentMask)
                val value = split[1].toLong()
                adresses.forEach { adr -> memory[adr] = value }
            }
        }
        return memory.values.sum()
    }

    private fun Long.applyMask(mask: String) = toString(2).padStart(36, '0').toCharArray()
        .mapIndexed { index, c -> if (mask[index] != 'X') mask[index] else c }.joinToString("").toLong(2)

    private fun Int.applyMask2(mask: String): List<Long> {
        val orig = toString(2).padStart(36, '0')
        val masks = Array((2.0).pow(mask.count { it == 'X' }).toInt()) { "" }
        var floatingProcessed = 0
        orig.forEachIndexed { index, c ->
            when (mask[index]) {
                '0' -> masks.indices.forEach { masks[it] = masks[it] + c }
                '1' -> masks.indices.forEach { masks[it] = masks[it] + '1' }
                'X' -> {
                    var floatStep = masks.size
                    var floatChar = '0'
                    var floatStepped = 0
                    repeat(floatingProcessed + 1) {
                        floatStep /= 2
                    }
                    floatingProcessed++

                    masks.indices.forEach {
                        masks[it] = masks[it] + floatChar
                        floatStepped++
                        if (floatStepped >= floatStep) {
                            floatStepped = 0
                            floatChar = if (floatChar == '0') '1' else '0'
                        }
                    }
                }
            }
        }
        return masks.map { it.toLong(2) }
    }
}