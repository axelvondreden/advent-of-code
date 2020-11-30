package y2016

import Day
import Utils

class Day04 : Day() {

    override val input = Utils.readStrings(2016, 4).map { it.dropLast(1).split("[") }

    private val rooms = input.map {
        val checksum = it[1]
        val split = it[0].lastIndexOf('-')
        val name = it[0].substring(0 until split)
        val sector = it[0].substring(split + 1).toInt()
        Room(name, sector, checksum)
    }

    override fun solve1() = rooms.filter { it.check() }.sumBy { it.sector }

    override fun solve2() = rooms.first { it.decrypt().contains("northpole") }.sector

    private fun Room.check(): Boolean {
        val cleanName = name.replace("-", "")
        val occurrences = cleanName.groupingBy { it }.eachCount()
        var check = ""
        for (i in occurrences.map { it.value }.maxOrNull()!! downTo 1) {
            val temp = occurrences.filter { it.value == i }.keys.sorted().joinToString("")
            check += temp
        }
        return check.substring(0..4) == checksum
    }

    private fun Room.decrypt(): String {
        val shift = sector % 26
        var ret = ""
        for (c in name) {
            if (c == '-') {
                ret += " "
            } else {
                var newC = c + shift
                while (newC > 122.toChar()) {
                    newC -= 26
                }
                ret += newC
            }
        }
        return ret
    }

    data class Room(val name: String, val sector: Int, val checksum: String)
}