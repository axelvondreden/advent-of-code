package y2023

import Day

class Day05 : Day<Unit>(2023, 5) {

    private lateinit var seeds: List<Long>

    private val hashingMap = hashMapOf<String, Pair<String, Set<HashingRange>>>()

    override fun List<String>.parse() = with(joinToString("\n")) {
        val blocks = split("\n\n")
        seeds = blocks.first().split(": ")[1].split(" ").map(String::toLong)

        blocks.drop(1).forEach { block ->
            val lines = block.trimEnd().split("\n")
            val (source, _, target) = lines.first().split(" ").first().split("-")
            val hashing = lines
                .drop(1)
                .map { line ->
                    line.split(" ")
                        .map(String::toLong)
                        .let { (target, source, range) -> HashingRange(source, target, range) }
                }.toSet()

            hashingMap[source] = target to hashing
        }
    }

    override fun solve1(input: Unit) = seeds.minOf { it.hash() }

    override fun solve2(input: Unit): Long {
        var seed = 1
        val chunked = seeds.chunked(2)
        return chunked.minOf { (source, range) ->
            var result = Long.MAX_VALUE
            var number = source
            val target = number + range

            do {
                if (number % 1000000 == 0L) {
                    println("Seed $seed/${chunked.size}: ${number.toDouble() / target}")
                }
                val value = number.hash()
                if (result > value) result = value
            } while (++number < target)
            seed++
            result
        }
    }

    private fun Long.hash(): Long {
        var phase = "seed"
        var value = this

        do {
            val (target, hashing) = hashingMap.getValue(phase)
            hashing.firstOrNull { value in it }?.let { value = it.hash(value) }
            phase = target
        } while (phase != "location")

        return value
    }

    private data class HashingRange(val source: Long, val target: Long, val range: Long) {
        operator fun contains(num: Long) = num in source until (source + range)

        fun hash(num: Long) = num + (target - source)
    }
}