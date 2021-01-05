package y2015

import Day
import utils.getAllDivisors

class Day20 : Day(2015, 20) {

    override val input = readString().toInt()

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
        val presentCounter = HashMap<Int, Int>()
        var houseNumber = 0
        val divisors = HashSet<Int>()
        do {
            houseNumber++
            divisors.clear()
            houseNumber.getAllDivisors(divisors)
            divisors.forEach { presentCounter[it] = presentCounter[it]?.plus(1) ?: 1 }
            val value =
                divisors.fold(0, { sum, divisor -> if (presentCounter[divisor] ?: 0 <= 50) sum + divisor else sum })
        } while (value < input / 11 && houseNumber < 1000000)
        return houseNumber
    }
}