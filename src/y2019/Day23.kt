package y2019

import Day
import Utils

class Day23 : Day() {

    override val input = Utils.readLongArray(2019, 23)

    private val comps = mutableMapOf<Int, IntCodeComputer>()
    private val buffer = mutableMapOf<Int, MutableList<Long>>()

    override fun solve1(): Int {
        for (i in 0 until 50) {
            comps[i] = IntCodeComputer(input.copyOf()).withInputFunction {
                if (buffer[i]!!.isNotEmpty()) buffer[i]!!.removeAt(0) else -1
            }
            buffer[i] = mutableListOf(i.toLong())
        }
        while (true) {
            for (i in 0 until 50) {
                val target = comps[i]!!.run(false)
                val x = comps[i]!!.run(false)
                val y = comps[i]!!.run(false)
                println("$target $x $y")
                if (target == 255L) return y.toInt()
                buffer[target.toInt()]!!.add(x)
                buffer[target.toInt()]!!.add(y)
            }
        }
    }

    override fun solve2(): Int {
        return 0
    }
}