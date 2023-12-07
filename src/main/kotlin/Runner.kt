import utils.IO
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList
import kotlin.system.measureNanoTime

const val firstYear = 2015
const val lastYear = 2023

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
        } catch (_: ClassNotFoundException) {
        }
    }
}

fun runLatest(year: Int) {
    (25 downTo 1).forEach {
        try {
            runDay(year, it, skipLongRunning)
            return
        } catch (_: ClassNotFoundException) {
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun runDay(year: Int, day: Int, skipSlow: Boolean = false) {
    val d = Class.forName("y$year.Day${day.toString().padStart(2, '0')}")?.getDeclaredConstructor()
            ?.newInstance() as Day<Any>
    println("Year $year Day $day:")

    val samples = IO.readSamples(year, day)
    println("Samples Part 1:")
    samples?.part1?.forEachIndexed { index, sample ->
        print("\t(${index + 1} / ${samples.part1.size}) Init[")
        val init = measureInit(d, sample.input.lines())
        print("${init.first.coloredTime()}] - Solve[")
        val result = runPart(d, 1, init.second, sample.solution)
        println("${result.first.coloredTime()}]: ${result.second}")
    }

    println("Samples Part 2:")
    samples?.part2?.forEachIndexed { index, sample ->
        print("\t(${index + 1} / ${samples.part2.size}) Init[")
        val init = measureInit(d, sample.input.lines())
        print("${init.first.coloredTime()}] - Solve[")
        val result = runPart(d, 2, init.second, sample.solution)
        println("${result.first.coloredTime()}]: ${result.second}")
    }

    var sumTime = 0.0
    val input = IO.readStrings(d.year, d.day)
    println("Real Input: ")
    print("\tInit[")
    val init = measureInit(d, input)
    println("${init.first.coloredTime()}]")
    sumTime += init.first
    print("\tPart 1[")
    if (skipSlow && year to day to 1 in skips) {
        println("${ANSI_RED}SKIPPED$ANSI_RESET]")
    } else {
        val result = runPart(d, 1, init.second, expected[Triple(d.year, d.day, 1)])
        println("${result.first.coloredTime()}]: ${result.second}")
        sumTime += result.first
    }

    print("\tPart 2[")
    if (skipSlow && year to day to 2 in skips) {
        println("${ANSI_RED}SKIPPED$ANSI_RESET]")
    } else {
        val result = runPart(d, 2, init.second, expected[Triple(d.year, d.day, 2)])
        println("${result.first.coloredTime()}]: ${result.second}")
        sumTime += result.first
    }

    println("\tTotal [${sumTime.coloredTime()}]")
    println("-".repeat(80))
}

private fun Double.color() = if (this <= 1) ANSI_GREEN else ANSI_RED

fun runPart(day: Day<Any>, part: Int, input: Any, expected: String?): Pair<Double, String> {
    var result: String
    val time = measureNanoTime { result = (if (part == 1) day.solve1(input) else day.solve2(input)).toString() } / 1000000000.0
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    if (isCorrect) correct++ else incorrect++
    val resultString = result + (if (isCorrect) "\t${ANSI_GREEN}SUCCESS$ANSI_RESET" else "\t${ANSI_RED}FAILED$ANSI_RESET")
    return time to resultString
}

private fun Double.coloredTime() = "${color()}${"%.6f".format(this)}s$ANSI_RESET"

fun measureInit(day: Day<Any>, input: List<String>): Pair<Double, Any> {
    var parsed: Any
    val time = measureNanoTime { with(day) { parsed = input.parse() } } / 1000000000.0
    return time to parsed
}

fun parseExpected() = getExpectedLines().associate { line ->
    val split = line.split(":")
    val part = split[0].split(",").map { it.toInt() }
    Triple(part[0], part[1], part[2]) to split[1]
}

fun getExpectedLines(): List<String> {
    Files.lines(Paths.get("src/main/resources/solutions.txt")).use { lines -> return lines.toList() }
}