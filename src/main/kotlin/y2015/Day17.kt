package y2015

import Day
import utils.Point
import utils.sumByLong

class Day17 : Day(2015, 17) {

    override val input = readStrings().mapIndexed { index, nr -> Point(index.toLong(), nr.toLong()) }

    private val combinations = mutableListOf<List<Point>>()

    override fun solve1(): Int {
        fill(mutableListOf(), 150)
        return combinations.size
    }

    override fun solve2(): Int {
        val min = combinations.map { it.size }.minOrNull()!!
        return combinations.count { it.size == min }
    }

    private fun fill(chain: MutableList<Point>, expected: Long) {
        val sum = chain.sumByLong { it.y }
        when {
            sum == expected && !combinations.containsList(chain) -> combinations.add(chain)
            sum > expected -> return
            else -> {
                val options = input.filter {
                    it.x > (chain.lastOrNull()?.x ?: -1) && it !in chain && sum + it.y <= expected
                }
                options.forEach { fill(chain.toTypedArray().copyOf().toMutableList().apply { add(it) }, expected) }
            }
        }
    }

    private fun MutableList<List<Point>>.containsList(search: List<Point>) = any { it.containsAll(search) }
}