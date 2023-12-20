package y2023

import Day

class Day20 : Day<List<String>>(2023, 20) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>): Int {
        val destinations = input.map { it.split(" -> ") }.associate { (name, destinations) ->
            name.replace("[%&]".toRegex(), "") to destinations.split(", ")
        }
        val modules = input.map { Module.from(it.split(" -> ").first()) }.associateBy { it.name } +
            destinations.values.flatten().filter { it !in destinations.keys }.associateWith { Module.Untyped(it) }


        for ((source, destinations) in destinations) {
            for (destination in destinations) {
                val module = modules.getValue(destination)
                if (module is Module.Conjunction) {
                    module.states[source] = false
                }
            }
        }
        var low = 0
        var high = 0

        repeat(1000) {
            var current = listOf(modules.getValue("broadcaster") to false)

            while (current.isNotEmpty()) {
                current = buildList {
                    for ((module, receivedPulse) in current) {
                        if (receivedPulse) high++ else low++

                        val output = module.output(receivedPulse) ?: continue
                        for (destination in destinations.getValue(module.name).map { modules.getValue(it) }) {
                            destination.receive(module.name, output)
                            add(destination to output)
                        }
                    }
                }
            }
        }

        return low * high
    }

    override suspend fun solve2(input: List<String>): Long {
        return 0L
    }

    private sealed class Module {
        abstract val name: String

        open fun receive(source: String, pulse: Boolean) {}

        open fun output(receivedPulse: Boolean): Boolean? = receivedPulse

        data class Broadcaster(override val name: String) : Module()

        data class FlipFlop(override val name: String, var state: Boolean) : Module() {
            override fun receive(source: String, pulse: Boolean) {
                if (!pulse) {
                    state = !state
                }
            }

            override fun output(receivedPulse: Boolean) = if (receivedPulse) null else state
        }

        data class Conjunction(override val name: String, val states: MutableMap<String, Boolean>) : Module() {
            override fun receive(source: String, pulse: Boolean) {
                states[source] = pulse
            }

            override fun output(receivedPulse: Boolean) = states.values.all { it }.not()
        }

        data class Untyped(override val name: String) : Module() {
            override fun output(receivedPulse: Boolean) = null
        }

        companion object {
            fun from(str: String): Module {
                return when (str.first()) {
                    '%' -> FlipFlop(name = str.drop(1), state = false)
                    '&' -> Conjunction(name = str.drop(1), states = mutableMapOf())
                    else -> Broadcaster(name = str)
                }
            }
        }
    }
}