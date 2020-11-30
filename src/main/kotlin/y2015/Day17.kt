package y2015

import Day
import Point
import Utils

class Day17 : Day() {

    override val input = Utils.readStrings(2015, 17).mapIndexed { index, nr -> Point(index, nr.toInt()) }

    private val combinations = mutableListOf<List<Point>>()

    override fun solve1(): Int {
        fill(mutableListOf(), 150)
        return combinations.size
    }

    override fun solve2(): Int {
        val min = combinations.map { it.size }.minOrNull()!!
        return combinations.count { it.size == min }
    }

    private fun fill(chain: MutableList<Point>, expected: Int) {
        val sum = chain.sumBy { it.y }
        when {
            sum == expected && !combinations.containsList(chain) -> combinations.add(chain)
            sum > expected -> return
            else -> {
                val options = input.filter {
                    it.x > (chain.lastOrNull()?.x ?: -1) && it !in chain && sum + it.y <= expected
                }
                for (option in options) {
                    val newChain = chain.toTypedArray().copyOf().toMutableList()
                    newChain.add(option)
                    fill(newChain, expected)
                }
            }
        }
    }

    private fun MutableList<List<Point>>.containsList(search: List<Point>) = this.any { it.containsAll(search) }
}