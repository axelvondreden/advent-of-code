package y2018

import Day
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Day04 : Day(2018, 4) {

    override val input = readStrings().sorted().parseGuards()

    override fun solve1(): Int {
        val guard = input.maxByOrNull { guard -> guard.sleepRanges.sumBy { it.minutes.count() } }
        val minute = guard!!.sleepRanges.flatMap { it.minutes }.groupBy { it }.maxByOrNull { it.value.size }!!.key
        return guard.id * minute
    }

    override fun solve2(): Int {
        var result = 0
        var maxSleepyMinutes = 0
        (0..59).forEach { minute ->
            input.forEach { guard ->
                val sleepyMinutes = guard.sleepRanges.count { minute in it.minutes }
                if (sleepyMinutes > maxSleepyMinutes) {
                    maxSleepyMinutes = sleepyMinutes
                    result = guard.id * minute
                }
            }
        }
        return result
    }

    private fun List<String>.parseGuards(): Set<Guard> {
        val guards = mutableSetOf<Guard>()
        var currentGuard: Guard? = null
        var currentFrom: LocalDateTime? = null
        forEach { line ->
            when {
                line.contains("Guard") -> {
                    val id = line.split("#")[1].split(" ")[0].toInt()
                    currentGuard = guards.firstOrNull { it.id == id } ?: Guard(id, emptyList()).also { guards.add(it) }
                }
                line.contains("falls asleep") -> {
                    currentFrom = LocalDateTime.parse(line.substring(1, 17), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                }
                line.contains("wakes up") -> {
                    val to = LocalDateTime.parse(line.substring(1, 17), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    currentGuard!!.sleepRanges = currentGuard!!.sleepRanges.plus(SleepRange(currentFrom!!, to))
                }
            }
        }
        return guards
    }

    data class Guard(val id: Int, var sleepRanges: List<SleepRange>) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Guard
            if (id != other.id) return false
            return true
        }

        override fun hashCode() = id
    }

    data class SleepRange(val from: LocalDateTime, val to: LocalDateTime) {
        val minutes get() = from.minute until to.minute
    }
}