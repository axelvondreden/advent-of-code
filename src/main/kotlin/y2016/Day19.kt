package y2016

import Day


class Day19 : Day<List<String>>(2016, 19) {

    override val input = readString().toInt()

    override fun solve1(input: List<String>): Int {
        val map = Array(input) { Elf(it + 1, 1) }
        var thief = 0
        while (true) {
            var target = if (thief < input - 1) thief + 1 else 0
            while (map[target].presents == 0) {
                if (target < input - 1) target++ else target = 0
            }
            val newAmount = map[thief].presents + map[target].presents
            map[thief].presents = newAmount
            map[target].presents = 0
            if (newAmount >= input) return thief + 1
            if (thief < input - 1) thief++ else thief = 0
            while (map[thief].presents == 0) {
                if (thief < input - 1) thief++ else thief = 0
            }
        }
    }

    override fun solve2(input: List<String>): Int {
        val map = (0 until input).map { Elf(it + 1, 1) }.toMutableList()
        var thief = 0
        while (map.size > 1) {
            var target = thief + map.size / 2
            if (target >= map.size) target -= map.size
            map[thief].presents = map[thief].presents + map[target].presents
            map.removeAt(target)
            if (target < thief) thief--
            if (thief < map.size - 1) thief++ else thief = 0
        }
        return map.first().nr
    }

    private data class Elf(val nr: Int, var presents: Int)
}