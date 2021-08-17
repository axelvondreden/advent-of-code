package y2016

import Day
import kotlin.math.abs

class Day11 : Day(2016, 11) {

    override val input = readStrings()

    override fun solve1(): String = input.collectState().calculateState().toString()

    override fun solve2(): String = input.mapIndexed { i, s ->
        when (i) {
            0 -> "$s An elerium generator. An elerium-compatible microchip. A dilithium generator. A dilithium-compatible microchip."
            else -> s
        }
    }.collectState().calculateState().toString()

    private data class State(
        var result: Int = 0,
        var floor: Int = 0,
        val last: MutableMap<String, Int> = mutableMapOf(),
        val gaps: MutableMap<Int, Int> = mutableMapOf()
    )

    private val matcher = Regex("([a-z]+)(\\sgenerator|-compatible\\smicrochip)")

    private fun List<String>.collectState() = collectIndexed(State()) { state, floor, line ->
        matcher.findAll(line).map { it.groups[0].toString() }.forEach { key ->
            state.result += (3 - floor) * 2

            if (!state.last.containsKey(key)) {
                state.last[key] = floor
            } else {
                val gap = abs(floor - state.last[key]!!)
                if (gap != 0) {
                    val gapValue = state.gaps.getOrPut(gap) { 0 }
                    state.gaps[gap] = gapValue + 1
                }
            }
        }
    }

    private fun State.calculateState() = when (last.size) {
        1 -> 3
        else -> {
            gaps.forEach { if (it.value % 2 == 0) result += (it.key * it.value - 1) * 2 }
            result - (3 * 3)
        }
    }

    private inline fun <T, R> List<T>.collectIndexed(collector: R, crossinline action: ((R, Int, T) -> Unit)): R {
        forEachIndexed { i, item -> action.invoke(collector, i, item) }
        return collector
    }
}