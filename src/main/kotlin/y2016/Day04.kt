package y2016

import Day

class Day04 : Day<List<List<String>>>(2016, 4) {

    override suspend fun List<String>.parse() = map { it.dropLast(1).split("[") }

    override suspend fun solve1(input: List<List<String>>): Int {
        val rooms = input.map {
            val checksum = it[1]
            val split = it[0].lastIndexOf('-')
            val name = it[0].substring(0 until split)
            val sector = it[0].substring(split + 1).toInt()
            Room(name, sector, checksum)
        }
        return rooms.filter { it.check() }.sumOf { it.sector }
    }

    override suspend fun solve2(input: List<List<String>>): Int {
        val rooms = input.map {
            val checksum = it[1]
            val split = it[0].lastIndexOf('-')
            val name = it[0].substring(0 until split)
            val sector = it[0].substring(split + 1).toInt()
            Room(name, sector, checksum)
        }
        return rooms.first { it.decrypt().contains("northpole") }.sector
    }


    private data class Room(val name: String, val sector: Int, val checksum: String) {
        fun check(): Boolean {
            val cleanName = name.replace("-", "")
            val occurrences = cleanName.groupingBy { it }.eachCount()
            var check = ""
            (occurrences.values.maxOrNull()!! downTo 1)
                .map { i -> occurrences.filter { it.value == i }.keys.sorted().joinToString("") }
                .forEach { check += it }
            return check.substring(0..4) == checksum
        }

        fun decrypt(): String {
            val shift = sector % 26
            var ret = ""
            name.forEach { c ->
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
    }
}