import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

val longRunning = setOf(
    2015 to 4 to 2,
    2015 to 22 to 1,
    2015 to 24 to 1,
    2016 to 5 to 1,
    2016 to 5 to 2,
    2019 to 3 to 1,
    2019 to 3 to 2,
    2019 to 7 to 2,
    2019 to 11 to 1,
    2019 to 11 to 2,
    2019 to 12 to 2,
    2019 to 14 to 2,
    2019 to 18 to 1,
    2019 to 18 to 2,
    2019 to 19 to 2
)

const val skipLongRunning = true

const val runLatest = true

fun main() {
    runDay(2019, 18)
    //(2015..2019).forEach { if (runLatest) runLatest(it) else run(it) }
}

fun run(year: Int) {
    println("\n*** Year $year ***")
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
    val d = Class.forName("y$year.Day${day.toString().padStart(2, '0')}")?.newInstance() as Day
    println("\nDay $day:")

    print("\tPart 1: ")
    if (skipLongRunning && year to day to 1 in longRunning) {
        println("SKIPPED")
    } else {
        println(" [${"%.6f".format(measureNanoTime { print(d.solve1()) } / 1000000000.0)} s] ")
    }

    print("\tPart 2: ")
    if (skipLongRunning && year to day to 2 in longRunning) {
        println("SKIPPED")
    } else {
        println(" [${"%.6f".format(measureNanoTime { print(d.solve2()) } / 1000000000.0)} s] ")
    }
}