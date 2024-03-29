package y2021

import Day

class Day02 : Day<List<String>>(2021, 2) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val sub = SimpleSubmarine()
        input.forEach { sub.step(it) }
        return sub.hpos * sub.depth
    }

    override suspend fun solve2(input: List<String>): Int {
        val sub = AimingSubmarine()
        input.forEach { sub.step(it) }
        return sub.hpos * sub.depth
    }

    abstract class Submarine {
        var hpos = 0
        var depth = 0
        var aim = 0
        abstract fun step(input: String)
    }

    class SimpleSubmarine : Submarine() {
        override fun step(input: String) {
            with (input.split(" ")) {
                when (this[0]) {
                    "forward" -> hpos += this[1].toInt()
                    "up" -> depth -= this[1].toInt()
                    "down" -> depth += this[1].toInt()
                }
            }
        }
    }

    class AimingSubmarine : Submarine() {
        override fun step(input: String) {
            with (input.split(" ")) {
                when (this[0]) {
                    "forward" -> {
                        hpos += this[1].toInt()
                        depth += aim * this[1].toInt()
                    }
                    "up" -> aim -= this[1].toInt()
                    "down" -> aim += this[1].toInt()
                }
            }
        }
    }
}