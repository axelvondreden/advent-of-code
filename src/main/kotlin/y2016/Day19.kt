package y2016

import Day
import java.util.*


class Day19 : Day<Int>(2016, 19) {

    override fun List<String>.parse() = first().toInt()

    override fun solve1(input: Int): Int {
        val elves = BitSet(input)
        elves.set(0, input)
        var position = 0
        repeat(input - 1) {
            val next = maxOf(elves.nextSetBit(position + 1), elves.nextSetBit(0))
            elves.clear(next)
            position = maxOf(elves.nextSetBit(next + 1), elves.nextSetBit(0))
        }

        return (position + 1)
    }

    override fun solve2(input: Int): Int {
        val elves = LinkedList((1..input).toList())
        var target = elves.listIterator(input / 2)
        target.next()
        for (i in input downTo 2) {
            target.remove()
            repeat(i % 2 + 1) {
                if (!target.hasNext()) {
                    target = elves.listIterator()
                }
                target.next()
            }
        }

        if (!target.hasNext()) {
            target = elves.listIterator()
        }

        return target.next()
    }
}