package y2023

import Day

class Day20 : Day<List<String>>(2023, 20) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>) = 0

    override suspend fun solve2(input: List<String>) = 0

    sealed class Module(val name: String, val inputs: List<String>, val outputs: List<String>) {

        abstract fun receive(modules: List<Module>, pulse: Pulse)

        fun send(modules: List<Module>, pulse: Pulse) {
            outputs.forEach { name ->
                modules.first { it.name == name }.receive(modules, pulse)
            }
        }

        class FlipFlopModule(name: String, descendants: List<String>, var on: Boolean = false) : Module(name, descendants) {
            override fun receive(modules: List<Module>, pulse: Pulse) {
                if (pulse == Pulse.LOW) {
                    on = !on
                    send(modules, if (on) Pulse.HIGH else Pulse.LOW)
                }
            }
        }

        class ConjunctionModel(name: String, descendants: List<String>) : Module(name, descendants) {

        }

        class Broadcasterodule(name: String, descendants: List<String>) : Module(name, descendants) {
            override fun receive(modules: List<Module>, pulse: Pulse) {
                send(modules, pulse)
            }
        }
    }

    enum class Pulse {
        LOW, HIGH
    }
}