package y2023

import Day
import utils.lcm

class Day20 : Day<List<String>>(2023, 20) {

    override suspend fun List<String>.parse() = this

    private fun List<String>.parseModules(): Map<String, Module> {
        val modules = map { line ->
            val name = line.substringBefore(" ").replace("%", "").replace("&", "")
            val outputs = line.substringAfter("-> ").split(", ")
            when (line[0]) {
                '%' -> Flipflop(name, outputs, false)
                '&' -> Conjunction(name, outputs)
                else -> Broadcaster(name, outputs)
            }
        }

        modules.filterIsInstance<Conjunction>().forEach { conjunction ->
            modules.filter { conjunction.name in it.outputs }.forEach { input ->
                conjunction.connectInput(input.name)
            }
        }
        return modules.associateBy { it.name }
    }

    override suspend fun solve1(input: List<String>): Long {
        val grid = input.parseModules()
        var lowPulses = 0L
        var highPulses = 0L

        val unprocessed = ArrayDeque<Signal>()

        repeat(1000) {
            unprocessed.add(Signal("button", "broadcaster", false))

            while (unprocessed.isNotEmpty()) {
                val signal = unprocessed.removeFirst()
                if (signal.highPulse) highPulses++ else lowPulses++

                unprocessed += grid[signal.target]?.processSignal(signal).orEmpty()
            }
        }

        return lowPulses * highPulses
    }

    override suspend fun solve2(input: List<String>): Long {
        val grid = input.parseModules()
        val moduleBeforeSink = grid.values.filterIsInstance<Conjunction>().single { it.outputs == listOf("rx") }
        val periodOfInputModules = mutableMapOf<String, Long>()

        generateSequence(1L, Long::inc)
            .takeWhile { periodOfInputModules.size != moduleBeforeSink.inputs.size }
            .forEach { buttonPress ->
                val unprocessed = ArrayDeque<Signal>()
                unprocessed.add(Signal("button", "broadcaster", false))

                while (unprocessed.isNotEmpty()) {
                    val signal = unprocessed.removeFirst()
                    unprocessed += grid[signal.target]?.processSignal(signal).orEmpty()

                    if (signal.highPulse && signal.target == moduleBeforeSink.name) {
                        periodOfInputModules[signal.source] = buttonPress
                    }
                }

            }

        return periodOfInputModules.values.reduce(::lcm)
    }


    sealed class Module(val name: String, val outputs: List<String>) {
        fun processSignal(inputSignal: Signal) = emitHighPulse(inputSignal)?.let { highPulse ->
            outputs.map { target -> Signal(name, target, highPulse) }
        }.orEmpty()

        protected abstract fun emitHighPulse(inputSignal: Signal): Boolean?

    }

    private class Broadcaster(name: String, outputs: List<String>) : Module(name, outputs) {
        override fun emitHighPulse(inputSignal: Signal) = inputSignal.highPulse
    }

    private class Flipflop(name: String, outputs: List<String>, var active: Boolean = false) : Module(name, outputs) {
        override fun emitHighPulse(inputSignal: Signal) = when {
            inputSignal.highPulse -> null
            else -> !active.also { active = !active }
        }
    }

    private class Conjunction(
        name: String,
        outputs: List<String>,
        val inputs: MutableMap<String, Boolean> = mutableMapOf()
    ) :
        Module(name, outputs) {
        override fun emitHighPulse(inputSignal: Signal): Boolean {
            inputs[inputSignal.source] = inputSignal.highPulse
            return inputs.any { !it.value }
        }

        fun connectInput(inputModule: String) {
            inputs[inputModule] = false
        }
    }

    data class Signal(val source: String, val target: String, val highPulse: Boolean)
}