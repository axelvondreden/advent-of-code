package y2023

import Day

class Day20 : Day<List<String>>(2023, 20) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>) = 0

    override suspend fun solve2(input: List<String>) = 0

    private fun pressButton(modules: List<Module>) {
        modules.first { it.name == "broadcaster" }.receive(modules, "", Pulse.LOW)
    }

    sealed class Module(val name: String, val inputs: List<String>, val outputs: List<String>) {

        abstract fun receive(modules: List<Module>, from: String, pulse: Pulse)

        fun send(modules: List<Module>, pulse: Pulse) {
            outputs.forEach { name ->
                modules.first { it.name == name }.receive(modules, this.name, pulse)
            }
        }

        class FlipFlopModule(name: String, inputs: List<String>, outputs: List<String>, var on: Boolean = false) : Module(name, inputs, outputs) {
            override fun receive(modules: List<Module>, from: String, pulse: Pulse) {
                if (pulse == Pulse.LOW) {
                    on = !on
                    send(modules, if (on) Pulse.HIGH else Pulse.LOW)
                }
            }
        }

        class ConjunctionModel(name: String, inputs: List<String>, outputs: List<String>) : Module(name, inputs, outputs) {
            private val buffer = inputs.associateWith { Pulse.LOW }.toMutableMap()

            override fun receive(modules: List<Module>, from: String, pulse: Pulse) {
                buffer[from] = pulse
                send(modules, if (buffer.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH)
            }
        }

        class Broadcasterodule(name: String, inputs: List<String>, outputs: List<String>) : Module(name, inputs, outputs) {
            override fun receive(modules: List<Module>, from: String, pulse: Pulse) {
                send(modules, pulse)
            }
        }
    }

    enum class Pulse {
        LOW, HIGH
    }
}