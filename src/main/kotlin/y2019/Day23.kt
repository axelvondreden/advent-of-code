package y2019

import Day

class Day23 : Day(2019, 23) {

    override val input = readLongArray()

    private val comps = mutableMapOf<Int, IntCodeComputer>()
    private val inputBuffer = mutableMapOf<Int, MutableList<Long>>()
    private val outputBuffer = mutableMapOf<Int, MutableList<Long>>()

    override fun solve1(): Int {
        initNetwork()
        while (true) {
            (0 until 50).forEach { i ->
                val reply = comps[i]!!.run()
                if (reply.hasOutput) {
                    outputBuffer[i]!!.add(reply.value)
                    if (outputBuffer[i]!!.size == 3) {
                        if (outputBuffer[i]!![0] == 255L) return outputBuffer[i]!![2].toInt()
                        inputBuffer[outputBuffer[i]!![0].toInt()]!!.add(outputBuffer[i]!![1])
                        inputBuffer[outputBuffer[i]!![0].toInt()]!!.add(outputBuffer[i]!![2])
                        outputBuffer[i]!!.clear()
                    }
                }
            }
        }
    }

    override fun solve2(): Int {
        initNetwork()
        var lastNatY = -1L
        var natMemory = Pair(0L, 0L)
        val lastActions = Array(50) { BooleanArray(5) { true } } //false -> received; true -> sent
        while (true) {
            (0 until 50).forEach { i ->
                val reply = comps[i]!!.run()
                lastActions[i] = booleanArrayOf(lastActions[i][1], lastActions[i][2], lastActions[i][3], lastActions[i][4], reply.hasOutput)
                if (reply.hasOutput) {
                    outputBuffer[i]!!.add(reply.value)
                    if (outputBuffer[i]!!.size == 3) {
                        if (outputBuffer[i]!![0] == 255L) {
                            natMemory = Pair(outputBuffer[i]!![1], outputBuffer[i]!![2])
                        } else {
                            inputBuffer[outputBuffer[i]!![0].toInt()]!!.add(outputBuffer[i]!![1])
                            inputBuffer[outputBuffer[i]!![0].toInt()]!!.add(outputBuffer[i]!![2])
                        }
                        outputBuffer[i]!!.clear()
                    }
                }
                if (lastActions.all { booleans -> booleans.all { !it } } && inputBuffer.values.all { it.isEmpty() }) {
                    if (lastNatY == natMemory.second) return natMemory.second.toInt()
                    inputBuffer[0]!!.add(natMemory.first)
                    inputBuffer[0]!!.add(natMemory.second)
                    lastActions[0][4] = true
                    lastNatY = natMemory.second
                }
            }
        }
    }

    private fun initNetwork() {
        (0 until 50).forEach { i ->
            comps[i] = IntCodeComputer(input.copyOf(), true, true).withInputFunction {
                if (inputBuffer[i]!!.isNotEmpty()) inputBuffer[i]!!.removeAt(0) else -1
            }
            inputBuffer[i] = mutableListOf(i.toLong())
            outputBuffer[i] = mutableListOf()
        }
    }
}