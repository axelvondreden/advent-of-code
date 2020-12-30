package y2015

import Day
import kotlin.math.max

class Day15 : Day(2015, 15) {

    override val input = readStrings().map {
        val split = it.split(", ")
        Ingredient(
            split[0].split(":")[0], split[0].split(" ")[2].toInt(), split[1].split(" ")[1].toInt(),
            split[2].split(" ")[1].toInt(), split[3].split(" ")[1].toInt(), split[4].split(" ")[1].toInt()
        )
    }

    override fun solve1(): Int {
        var maxScore = 0
        for (i in 0..100) {
            for (j in 0..100 - i) {
                for (k in 0..100 - i - j) {
                    val m = 100 - i - j - k
                    val score = Cookie(
                        mapOf(
                            input[0] to i,
                            input[1] to j,
                            input[2] to k,
                            input[3] to m
                        )
                    ).getScore()
                    maxScore = max(maxScore, score)
                }
            }
        }
        return maxScore
    }

    override fun solve2(): Int {
        var maxScore = 0
        for (i in 0..100) {
            for (j in 0..100 - i) {
                for (k in 0..100 - i - j) {
                    val m = 100 - i - j - k
                    val cookie = Cookie(
                        mapOf(
                            input[0] to i,
                            input[1] to j,
                            input[2] to k,
                            input[3] to m
                        )
                    )
                    if (cookie.getCalories() == 500) {
                        maxScore = max(maxScore, cookie.getScore())
                    }
                }
            }
        }
        return maxScore
    }

    class Cookie(private val ings: Map<Ingredient, Int>) {

        fun getScore(): Int {
            val capacity = ings.entries.sumBy { it.value * it.key.capacity }
            val durability = ings.entries.sumBy { it.value * it.key.durability }
            val flavor = ings.entries.sumBy { it.value * it.key.flavor }
            val texture = ings.entries.sumBy { it.value * it.key.texture }
            return max(capacity, 0) * max(durability, 0) * max(flavor, 0) * max(texture, 0)
        }

        fun getCalories() = ings.entries.sumBy { it.value * it.key.calories }
    }

    data class Ingredient(
        val name: String,
        val capacity: Int,
        val durability: Int,
        val flavor: Int,
        val texture: Int,
        val calories: Int
    )
}