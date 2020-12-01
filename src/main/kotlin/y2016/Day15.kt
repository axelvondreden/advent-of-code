package y2016

import Day

class Day15 : Day() {

    override val input = Utils.readStrings(2016, 15)

    override fun solve1(): Int {
        var time = -1
        var found = false
        while (!found) {
            time++
            found = simulate(parseDisks(input), time)
        }
        return time
    }

    override fun solve2(): Int {
        return 0
    }

    private fun simulate(disks: List<Disk>, time: Int): Boolean {
        disks.forEach { disk -> repeat(time) { disk.tick() } }
        var position = 0
        while (position < disks.size) {
            position++
            disks.forEach { it.tick() }
            if (disks[position - 1].current != 0) return false
        }
        return true
    }

    private fun parseDisks(input: List<String>) = input.map {
        val split = it.split(" ")
        Disk(split[3].toInt(), split[11].dropLast(1).toInt())
    }

    class Disk(private val positions: Int, var current: Int) {

        fun tick() {
            current++
            if (current == positions) {
                current = 0
            }
        }
    }
}