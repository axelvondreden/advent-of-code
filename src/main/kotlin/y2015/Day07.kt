package y2015

import Day
import utils.IO

@ExperimentalUnsignedTypes
@Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")
class Day07 : Day() {

    override val input = IO.readStrings(2015, 7).map { it.split(" -> ") }

    private val firstA = buildWires(input).getValue("a").get()


    override fun solve1() = firstA

    override fun solve2() = buildWires(input).apply { get("b")!!.a = Value(firstA) }.getValue("a").get()

    private fun buildWires(instructions: List<List<String>>): Map<String, Wire> {
        val wires = mutableMapOf<String, Wire>()
        for (instruction in instructions) {
            val input = instruction[0]
            var source: IOElement
            if (!input.contains(" ")) {
                source = if (input.toIntOrNull() == null) wires.getWire(input) else Value(
                    input.toUShort()
                )
            } else {
                val inputs = input.split(" ")
                when {
                    inputs[0] == "NOT" -> source = NotGate(wires.getWire(inputs[1]))
                    inputs[1] == "AND" -> source = AndGate(
                        if (inputs[0].toIntOrNull() == null) wires.getWire(inputs[0]) else Value(
                            inputs[0].toUShort()
                        ),
                        if (inputs[2].toIntOrNull() == null) wires.getWire(inputs[2]) else Value(
                            inputs[2].toUShort()
                        )
                    )
                    inputs[1] == "OR" -> {
                        source = OrGate(
                            if (inputs[0].toIntOrNull() == null) wires.getWire(inputs[0]) else Value(
                                inputs[0].toUShort()
                            ),
                            if (inputs[2].toIntOrNull() == null) wires.getWire(inputs[2]) else Value(
                                inputs[2].toUShort()
                            )
                        )
                    }
                    inputs[1] == "LSHIFT" -> source = LShiftGate(
                        wires.getWire(inputs[0]),
                        Value(inputs[2].toUShort())
                    )
                    inputs[1] == "RSHIFT" -> source = RShiftGate(
                        wires.getWire(inputs[0]),
                        Value(inputs[2].toUShort())
                    )
                    else -> throw Error("unknown opcode")
                }
            }

            val wire = wires.getOrDefault(
                instruction[1],
                Wire(Value(0u))
            )
            wire.a = source
            wires[instruction[1]] = wire
        }
        return wires
    }

    private fun MutableMap<String, Wire>.getWire(i: String): Wire {
        return getOrPut(i, { Wire(Value(0u)) })
    }

    abstract class IOElement {
        private var cache: UShort? = null
        abstract val getter: () -> UShort

        fun get(): UShort {
            if (cache == null) {
                cache = getter.invoke()
            }
            return cache!!
        }
    }

    class Wire(var a: IOElement) : IOElement() {
        override val getter = { a.get() }
    }

    class AndGate(private val a: IOElement, private val b: IOElement) : IOElement() {
        override val getter = { a.get() and b.get() }
    }

    class LShiftGate(private val a: Wire, private val b: Value) : IOElement() {
        override val getter = { (a.get().toInt() shl b.get().toInt()).toUShort() }
    }

    class NotGate(private val a: IOElement) : IOElement() {
        override val getter = { a.get().inv() }
    }

    class OrGate(private val a: IOElement, private val b: IOElement) : IOElement() {
        override val getter = { a.get() or b.get() }
    }

    class RShiftGate(private val a: Wire, private val b: Value) : IOElement() {
        override val getter = { (a.get().toInt() shr b.get().toInt()).toUShort() }
    }

    class Value(private val a: UShort) : IOElement() {
        override val getter = { a }
    }
}