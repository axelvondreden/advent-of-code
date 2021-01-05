package y2015

import Day

class Day14 : Day(2015, 14) {

    override val input = readStrings().map { it.split(" ") }

    private val reindeers = input.map { it[0] to Reindeer(it[3].toInt(), it[6].toInt(), it[13].toInt()) }.toMap()

    override fun solve1() = reindeers.values.map { it.getDistance(2503) }.maxOrNull()!!

    override fun solve2(): Int {
        val scores = reindeers.keys.map { it to 0 }.toMap().toMutableMap()
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