package y2019

import Day
import Utils
import kotlin.math.abs

class Day22 : Day() {

    override val input = Utils.readStrings(2019, 22)

    override fun solve1(): Int {
        var deck = (0..10006).toList().toIntArray()
        for (inst in input) {
            deck = when {
                inst.startsWith("cut") -> deck.cut(inst.split(" ")[1].toInt())
                inst.startsWith("deal with") -> deck.dealIncrement(inst.split(" ")[3].toInt())
                else -> deck.reversedArray()
            }
        }
        return deck.indexOf(2019)
    }

    override fun solve2(): Int {
        return 0
    }

    private fun IntArray.cut(n: Int): IntArray {
        val index = if (n > 0) n else size - abs(n)
        val start = drop(index)
        val end = slice(0 until index)
        return start.toMutableList().apply { addAll(end) }.toIntArray()
    }

    private fun IntArray.dealIncrement(n: Int): IntArray {
        val arr = copyOf()
        var index = 0
        for (i in indices) {
            arr[index % size] = get(i)
            index += n
        }
        return arr
    }
}