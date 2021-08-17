import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList
import kotlin.system.measureNanoTime

const val firstYear = 2015
const val lastYear = 2020

val skips = setOf(
    2015 to 22 to 1,
    2016 to 5 to 2,
    2016 to 19 to 2,
    2016 to 23 to 2,
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

val expected = parseExpected()

var correct = 0
var incorrect = 0

/**
 * default: run all
 * -y $year: run for year
 * -d $year $day: run for day
 * -i $FilePackage$ $FileNameWithoutAllExtensions$: IntelliJ program args for running current File
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        (firstYear..lastYear).forEach { run(it) }
        return
    }
    when (args[0]) {
        "-y" -> args.getOrNull(1)?.toIntOrNull()?.takeIf { it in firstYear..lastYear }?.let { run(it) }
        "-d" -> runDay(args[1].toInt(), args[2].toInt())
        "-i" -> runDay(args[1].drop(1).toInt(), args[2].drop(3).toInt())
        else -> return
    }
    println("Correct: $correct $ANSI_GREEN✔$ANSI_RESET")
    println("Incorrect: $incorrect $ANSI_RED⚠$ANSI_RESET")
}

fun run(year: Int) {
    (1..25).forEach {
        try {
            runDay(year, it, skipLongRunning)
        } catch (e: ClassNotFoundException) {
        }
    }
}

fun runLatest(year: Int) {
    (25 downTo 1).forEach {
        try {
            runDay(year, it, skipLongRunning)
            return
        } catch (e: ClassNotFoundException) {
        }
    }
}

fun runDay(year: Int, day: Int, skipSlow: Boolean = false) {
    var sumTime = 0.0
    var d: Day?
    val constTime = measureNanoTime {
        d = Class.forName("y$year.Day${day.toString().padStart(2, '0')}")?.getDeclaredConstructor()
            ?.newInstance() as Day
    } / 1000000000.0
    sumTime += constTime
    println("Year $year Day $day:")

    print("\tPart 1: ")
    if (skipSlow && year to day to 1 in skips) println("${ANSI_RED}SKIPPED$ANSI_RESET") else sumTime += runPart(d, 1)

    print("\tPart 2: ")
    if (skipSlow && year to day to 2 in skips) println("${ANSI_RED}SKIPPED$ANSI_RESET") else sumTime += runPart(d, 2)
    val color = if (sumTime <= 1) ANSI_GREEN else ANSI_RED
    println("Sum: [$color${"%.6f".format(sumTime)} s$ANSI_RESET]")
    println("-".repeat(40))
}

fun runPart(day: Day?, part: Int): Double {
    var result: String
    val expected = expected[Triple(day?.year, day?.day, part)]
    val time = measureNanoTime { result = (if (part == 1) day?.solve1() else day?.solve2()).toString() } / 1000000000.0
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    if (isCorrect) correct++ else incorrect++
    print((if (isCorrect) "$ANSI_GREEN✔$ANSI_RESET " else "$ANSI_RED⚠$ANSI_RESET ") + result)
    val color = if (time <= 1) ANSI_GREEN else ANSI_RED
    println(" [$color${"%.6f".format(time)} s$ANSI_RESET] ")
    return time
}

fun parseExpected() = getExpectedLines().associate { line ->
    val split = line.split(":")
    val part = split[0].split(",").map { it.toInt() }
    Triple(part[0], part[1], part[2]) to split[1]
}

fun getExpectedLines(): List<String> {
    Files.lines(Paths.get("src/main/resources/solutions.txt")).use { lines -> return lines.toList() }
}