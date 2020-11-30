package y2015

import Day
import Utils

class Day13 : Day() {

    override val input = Utils.readStrings(2015, 13).map { it.dropLast(1).split(" ") }

    private val happinessMap = input.map { Pair(it[0], it[10]) to it[3].toInt() * if (it[2] == "gain") 1 else -1 }.toMap().toMutableMap()

    override fun solve1() = getMaxHappiness(happinessMap)

    override fun solve2(): Int {
        happinessMap.keys.map { it.first }.distinct().forEach { dude ->
            happinessMap[Pair(dude, "me")] = 0
            happinessMap[Pair("me", dude)] = 0
        }
        return getMaxHappiness(happinessMap)
    }

    private fun getMaxHappiness(map: Map<Pair<String, String>, Int>): Int {
        val dudes = map.keys.map { it.first }.distinct()
        val allCombos = Utils.permute(dudes)
        return allCombos.map { combo ->
            (1 until combo.size).sumBy { map.getValue(Pair(combo[it], combo[it - 1])) + map.getValue(Pair(combo[it - 1], combo[it])) }
            + map.getValue(Pair(combo.first(), combo.last())) + map.getValue(Pair(combo.last(), combo.first()))
        }.maxOrNull()!!
    }
}