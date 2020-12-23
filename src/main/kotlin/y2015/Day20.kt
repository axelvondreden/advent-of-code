package y2015

import Day
import utils.getAllDivisors

class Day20 : Day() {

    override val input = 36000000

    override fun solve1(): Int {
        var houseNumber = 1
        val divisors: MutableSet<Int> = HashSet()
        do {
            houseNumber++
            divisors.clear()
            houseNumber.getAllDivisors(divisors)
        } while (divisors.sum() < input / 10 && houseNumber < 1000000)
        return houseNumber
    }

    override fun solve2(): Int {
        val presentCounter: MutableMap<Int, Int> = HashMap()
        var houseNumber = 0
        val divisors: MutableSet<Int> = HashSet()
        do {
            houseNumber++
            divisors.clear()
            houseNumber.getAllDivisors(divisors)
            divisors.forEach { presentCounter[it] = presentCounter[it]?.plus(1) ?: 1 }
            val value = divisors.fold(0, { sum, divisor -> if (presentCounter[divisor] ?: 0 <= 50) sum + divisor else sum })
        } while (value < input / 11 && houseNumber < 1000000)
        return houseNumber
    }
}