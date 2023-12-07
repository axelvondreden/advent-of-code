package y2017

import Day
import kotlin.math.max

class Day24 : Day<Any?>(2017, 24) {

    override val input = readStrings().map {
        val split = it.split("/")
        Component(split[0].toInt(), split[1].toInt())
    }.toSet()

    override fun solve1(input: List<String>) = findAllBridges(input, emptyList(), 0).maxOf { bridge -> bridge.sumOf { it.port1 + it.port2 } }

    override fun solve2(input: List<String>): Int {
        val bridges = findAllBridges(input, emptyList(), 0)
        val maxSize = bridges.maxOf { it.size }
        return bridges.filter { it.size == maxSize }.maxOf { bridge -> bridge.sumOf { it.port1 + it.port2 } }
    }

    private fun findAllBridges(components: Set<Component>, currentBridge: List<Component>, nextPort: Int): List<List<Component>> {
        val compatible = components.filter { it.canConnect(nextPort) }
        return when (compatible.size) {
            0 -> listOf(currentBridge)
            else -> compatible.flatMap {
                val newComponents = components.minus(it)
                val newBridge = currentBridge.plus(it)
                findAllBridges(newComponents, newBridge, if (it.port1 == nextPort) it.port2 else it.port1)
            }
        }
    }

    private fun List<Component>.findLastConnector(): Int {
        val lastPort1 = last().port1
        val lastPort2 = last().port2
        if (size == 1) return max(lastPort1, lastPort2)
        else {
            val secondToLast = get(size - 2)
            if (secondToLast.port1 == lastPort1 || secondToLast.port2 == lastPort1) return lastPort2
            return lastPort1
        }
    }

    data class Component(val port1: Int, val port2: Int) {
        fun canConnect(port: Int) = port == port1 || port == port2
    }
}
