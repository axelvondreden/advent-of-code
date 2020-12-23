import kotlin.system.measureNanoTime

val longRunning = setOf(
    2015 to 4 to 2,
    2015 to 22 to 1,
    2015 to 24 to 1,
    2016 to 5 to 1,
    2016 to 5 to 2,
    2016 to 14 to 2,
    2016 to 23 to 2,
    2016 to 19 to 2,
    2019 to 3 to 1,
    2019 to 3 to 2,
    2019 to 7 to 2,
    2019 to 11 to 1,
    2019 to 11 to 2,
    2019 to 12 to 2,
    2019 to 14 to 2,
    2019 to 18 to 1,
    2019 to 18 to 2,
    2019 to 19 to 2,
    2019 to 25 to 1
)

const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_RESET = "\u001B[0m"

const val skipLongRunning = true

const val runLatest = true

fun main() {
    (2015..2020).forEach { if (runLatest) runLatest(it) else run(it) }
}

fun run(year: Int) {
    (1..25).forEach {
        try {
            runDay(year, it)
        } catch (e: ClassNotFoundException) {
        }
    }
}

fun runLatest(year: Int) {
    (25 downTo 1).forEach {
        try {
            runDay(year, it)
            return
        } catch (e: ClassNotFoundException) {
        }
    }
}

fun runDay(year: Int, day: Int) {
    var sumTime = 0.0
    var d: Day?
    val constTime = measureNanoTime {
        d = Class.forName("y$year.Day${day.toString().padStart(2, '0')}")?.newInstance() as Day
    } / 1000000000.0
    sumTime += constTime
    println("Year $year Day $day:")

    print("\tPart 1: ")
    if (skipLongRunning && year to day to 1 in longRunning) {
        println("${ANSI_RED}SKIPPED$ANSI_RESET")
    } else {
        val time = measureNanoTime { print(d?.solve1()) } / 1000000000.0
        sumTime += time
        val color = if (time <= 1) ANSI_GREEN else ANSI_RED
        println(" [$color${"%.6f".format(time)} s$ANSI_RESET] ")
    }

    print("\tPart 2: ")
    if (skipLongRunning && year to day to 2 in longRunning) {
        println("${ANSI_RED}SKIPPED$ANSI_RESET")
    } else {
        val time = measureNanoTime { print(d?.solve2()) } / 1000000000.0
        sumTime += time
        val color = if (time <= 1) ANSI_GREEN else ANSI_RED
        println(" [$color${"%.6f".format(time)} s$ANSI_RESET] ")
    }
    val color = if (sumTime <= 1) ANSI_GREEN else ANSI_RED
    println("Sum: [$color${"%.6f".format(sumTime)} s$ANSI_RESET]")
    println("-".repeat(40))
}