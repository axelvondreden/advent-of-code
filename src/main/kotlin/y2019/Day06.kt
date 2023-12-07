package y2019

import Day

class Day06 : Day<List<String>>(2019, 6) {

    override val input = readStrings().map { it.split(")") }

    private val map = mutableMapOf<String, ArrayList<String>>()

    override fun solve1(input: List<String>): Int {
        input.forEach { line -> map[line[0]] = map.getOrDefault(line[0], ArrayList()).apply { add(line[1]) } }
        return countOrbits()
    }

    override fun solve2(input: List<String>) = getDistanceToSanta()

    private fun countOrbits(): Int {
        val distinct = map.values.flatten()
        var count = 0
        distinct.forEach { planet ->
            var origin = map.entries.firstOrNull { it.value.contains(planet) }
            while (origin != null) {
                count++
                origin = map.entries.firstOrNull { it.value.contains(origin!!.key) }
            }
        }
        return count
    }

    private fun getDistanceToSanta(): Int {
        val list1 = emptyList<String>().toMutableList()
        var planet = map.entries.firstOrNull { it.value.contains("YOU") }
        while (planet != null) {
            list1.add(planet.key)
            planet = map.entries.firstOrNull { it.value.contains(planet!!.key) }
        }
        var count = 0
        planet = map.entries.firstOrNull { it.value.contains("SAN") }
        while (planet != null) {
            if (list1.contains(planet.key)) return count + list1.indexOf(planet.key)
            count++
            planet = map.entries.firstOrNull { it.value.contains(planet!!.key) }
        }
        return 0
    }
}