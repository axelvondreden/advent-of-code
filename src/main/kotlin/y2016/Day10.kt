package y2016

import Day

class Day10 : Day(2016, 10) {

    override val input = readStrings()

    private val outputs = parseOutputs()

    private val bots = parseBots()

    init {
        linkBots()
        insertValues()
    }

    override fun solve1() = part1BotNr!!

    override fun solve2() = outputs.first { it.nr == 0 }.values.first() *
            outputs.first { it.nr == 1 }.values.first() * outputs.first { it.nr == 2 }.values.first()

    private fun parseOutputs(): Set<Output> {
        val outputs = mutableSetOf<Output>()
        input.filter { it.contains("to output") }.forEach { line ->
            val split = line.split("output")
            split.forEach {
                val nr = it.trim().split(" ")[0].toIntOrNull()
                if (nr != null) outputs.add(Output(nr))
            }
        }
        return outputs
    }

    private fun parseBots(): Set<Bot> {
        val bots = mutableSetOf<Bot>()
        input.filter { it.contains("bot") }.forEach { line ->
            val split = line.split("bot")
            split.forEach {
                val nr = it.trim().split(" ")[0].toIntOrNull()
                if (nr != null) bots.add(Bot(nr, null, null))
            }
        }
        return bots
    }

    private fun linkBots() {
        input.filter { it.startsWith("bot") }.forEach { line ->
            val split = line.split(" ")
            val sourceNr = split[1].toInt()
            val sourceBot = bots.first { it.nr == sourceNr }
            val lowTargetNr = split[6].toInt()
            val highTargetNr = split[11].toInt()
            sourceBot.lowTarget = (if (split[5] == "bot") bots else outputs).first { it.nr == lowTargetNr }
            sourceBot.highTarget = (if (split[10] == "bot") bots else outputs).first { it.nr == highTargetNr }
        }
    }

    private fun insertValues() {
        input.filter { it.startsWith("value") }.forEach { line ->
            val split = line.split(" ")
            val value = split[1].toInt()
            val botNr = split[5].toInt()
            bots.first { it.nr == botNr }.addValue(value)
        }
    }

    private companion object {
        const val part1LowerNr = 17
        const val part1HigherNr = 61
        private var part1BotNr: Int? = null
    }

    private abstract class Target(val nr: Int) {
        val values = mutableSetOf<Int>()

        abstract fun addValue(value: Int)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Target
            if (nr != other.nr) return false
            return true
        }

        override fun hashCode() = nr
    }

    private class Bot(nr: Int, var lowTarget: Target?, var highTarget: Target?): Target(nr) {
        override fun addValue(value: Int) {
            values.add(value)
            if (values.size == 2) proceed()
        }

        private fun proceed() {
            val low = values.minOrNull()!!
            val high = values.maxOrNull()!!
            if (low == part1LowerNr && high == part1HigherNr) part1BotNr = nr
            values.clear()
            lowTarget!!.addValue(low)
            highTarget!!.addValue(high)
        }
    }

    private class Output(nr: Int): Target(nr) {
        override fun addValue(value: Int) {
            values.clear()
            values.add(value)
        }
    }
}