package y2015

import Day

class Day14 : Day<List<List<String>>>(2015, 14) {

    override fun List<String>.parse() = map { it.split(" ") }

    override fun solve1(input: List<List<String>>) = input.associate {
        it[0] to Reindeer(it[3].toInt(), it[6].toInt(), it[13].toInt())
    }.values.maxOf { it.getDistance(2503) }

    override fun solve2(input: List<List<String>>): Int {
        val reindeers = input.associate { it[0] to Reindeer(it[3].toInt(), it[6].toInt(), it[13].toInt()) }
        val scores = reindeers.keys.associateWith { 0 }.toMutableMap()
        (1..2503).forEach { i ->
            val max = reindeers.map { it.value.getDistance(i) }.maxOrNull()
            reindeers.filter { it.value.getDistance(i) == max }.keys.forEach { leader ->
                scores[leader] = scores[leader]!! + 1
            }
        }
        return scores.values.maxOrNull()!!
    }

    private class Reindeer(private val speed: Int, private val flyTime: Int, private val restTime: Int) {

        fun getDistance(time: Int): Int {
            var distance = 0
            var elapsed = 0
            var fly = 0
            var rest = restTime
            while (elapsed < time) {
                if (fly < flyTime) {
                    fly++
                    distance += speed
                    if (fly == flyTime) rest = 0
                } else if (rest < restTime) {
                    rest++
                    if (rest == restTime) fly = 0
                }
                elapsed++
            }
            return distance
        }
    }
}