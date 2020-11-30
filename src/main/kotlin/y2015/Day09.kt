package y2015

import Day
import Utils

class Day09 : Day() {

    private val locations = mutableListOf<Location>()
    private val distances = mutableListOf<Distance>()

    override val input = Utils.readStrings(2015, 9).map { it.split(" ") }

    init {
        for (entry in input) {
            distances += Distance(
                Location(entry[0]).also { locations += it },
                Location(entry[2]).also { locations += it },
                entry[4].toInt()
            )
        }
    }

    private val nav = Navigation(distances)
    private val allRoutes = Utils.permute(locations.distinct())

    override fun solve1() = nav.getLength(allRoutes.filter { nav.isPathPossible(it.toTypedArray()) }.minByOrNull { nav.getLength(it.toTypedArray()) }!!.toTypedArray())

    override fun solve2() = nav.getLength(allRoutes.filter { nav.isPathPossible(it.toTypedArray()) }.maxByOrNull { nav.getLength(it.toTypedArray()) }!!.toTypedArray())

    class Navigation(private val distances: List<Distance>) {

        fun isPathPossible(route: Array<Location>) = (1 until route.size).none { i -> distances.none { it.from == route[i - 1] && it.to == route[i] || (it.from == route[i] && it.to == route[i - 1]) } }

        fun getLength(route: Array<Location>) = (1 until route.size).sumBy { i -> distances.first { it.from == route[i - 1] && it.to == route[i] || (it.from == route[i] && it.to == route[i - 1]) }.distance }
    }

    data class Location(val name: String)

    data class Distance(val from: Location, val to: Location, val distance: Int)
}