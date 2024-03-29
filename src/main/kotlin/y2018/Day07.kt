package y2018

import Day

class Day07 : Day<Set<Day07.Rule>>(2018, 7) {

    override suspend fun List<String>.parse() = map {
        val split = it.split(" ")
        Rule(split[7][0], split[1][0])
    }.toSet()

    private var minutesPassed = 0

    override suspend fun solve1(input: Set<Rule>) = simulateWork(input)

    override suspend fun solve2(input: Set<Rule>): Int {
        minutesPassed = 0
        simulateWork(input, 5)
        return minutesPassed
    }

    private fun simulateWork(input: Set<Rule>, workerAmount: Int = 1): String {
        val workers = List(workerAmount) { Worker() }
        val done = mutableListOf<Char>()
        val working = mutableSetOf<Char>()
        val todo = input.map { it.step }.plus(input.map { it.prerequisite }).toMutableSet()
        val targetSize = todo.size
        while (done.size < targetSize) {
            minutesPassed++
            workers.filter { it.currentStep == null }.forEach { worker ->
                val possible = mutableSetOf<Char>()
                todo.forEach { step ->
                    val rules = input.filter { it.step == step }
                    if (rules.all { it.prerequisite in done }) {
                        possible.add(step)
                    }
                }
                if (possible.isNotEmpty()) {
                    val next = possible.minOrNull()!!
                    todo.remove(next)
                    working.add(next)
                    worker.start(next)
                }
            }
            workers.filter { it.currentStep != null }.forEach {
                if (it.work()) {
                    working.remove(it.currentStep)
                    done.add(it.currentStep!!)
                    it.currentStep = null
                }
            }
        }
        return done.joinToString("")
    }

    data class Rule(val step: Char, val prerequisite: Char)

    private class Worker {

        var currentStep: Char? = null
        private var workCounter = 0

        fun start(step: Char) {
            currentStep = step
            workCounter = 0
        }

        fun work(): Boolean {
            if (currentStep == null) return true
            workCounter++
            return workCounter >= 61 + (currentStep!! - 'A')
        }
    }
}