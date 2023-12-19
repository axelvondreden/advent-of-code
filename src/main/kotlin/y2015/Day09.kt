package y2015

import Day
import utils.permute

class Day09 : Day<List<List<String>>>(2015, 9) {

    private val locations = mutableListOf<Location>()
    private val distances = mutableListOf<Distance>()

    override suspend fun List<String>.parse() = map { line ->
        line.split(" ").also { split ->
            distances += Distance(
                Location(split[0]).also { locations += it },
                Location(split[2]).also { locations += it },
                split[4].toInt()
            )
        }
    }

    override suspend fun solve1(input: List<List<String>>): Int {
        val nav = Navigation(distances)
        val allRoutes = locations.distinct().permute()
        return nav.getLength(allRoutes.filter { nav.isPathPossible(it.toTypedArray()) }
            .minByOrNull { nav.getLength(it.toTypedArray()) }!!.toTypedArray())

    }
    override suspend fun solve2(input: List<List<String>>): Int {
        val nav = Navigation(distances)
        val allRoutes = locations.distinct().permute()
        return nav.getLength(allRoutes.filter { nav.isPathPossible(it.toTypedArray()) }
            .maxByOrNull { nav.getLength(it.toTypedArray()) }!!.toTypedArray())
    }
    private class Navigation(private val distances: List<Distance>) {

        fun isPathPossible(route: Array<Location>) = (1 until route.size).none { i ->
            distances.none { it.from == route[i - 1] && it.to == route[i] || (it.from == route[i] && it.to == route[i - 1]) }
        }

        fun getLength(route: Array<Location>) = (1 until route.size).sumOf { i ->
            distances.first {
                it.from == route[i - 1] && it.to == route[i] || (it.from == route[i] && it.to == route[i - 1])
            }.distance
        }
    }

    private data class Location(val name: String)

    private data class Distance(val from: Location, val to: Location, val distance: Int)
}