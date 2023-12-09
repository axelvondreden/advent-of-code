import com.github.ajalt.mordant.rendering.BorderType.Companion.SQUARE_DOUBLE_SECTION_SEPARATOR
import com.github.ajalt.mordant.rendering.TextAlign
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import utils.IO
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList
import kotlin.system.measureNanoTime

val years = 2015..2023

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
 * [no args]: ask for input in terminal
 * -y $year: run all for year
 * -d $year $day: run single day
 * -i $FilePackage$ $FileNameWithoutAllExtensions$: IntelliJ program args for running current File
 */
fun main(args: Array<String>) {
    val t = Terminal()
    val width = t.info.width
    t.println("${brightBlue("Width")}: $width")
    t.println(
        table {
            borderType = SQUARE_DOUBLE_SECTION_SEPARATOR
            borderStyle = rgb("#4b25b9")
            align = TextAlign.RIGHT
            tableBorders = Borders.NONE
            header {
                style = brightRed + TextStyles.bold
                row {
                    cellBorders = Borders.NONE
                    cells("", "", "", "")
                    cell("Percent Change") {
                        columnSpan = 2
                        align = TextAlign.CENTER
                    }
                }
                row("", "2020", "2021", "2022", "2020-21", "2021-21") { cellBorders = Borders.BOTTOM }
            }
            body {
                style = green
                column(0) {
                    align = TextAlign.LEFT
                    cellBorders = Borders.ALL
                    style = brightBlue
                }
                column(4) {
                    cellBorders = Borders.LEFT_BOTTOM
                    style = brightBlue
                }
                column(5) {
                    style = brightBlue
                }
                rowStyles(TextStyle(), TextStyles.dim.style)
                cellBorders = Borders.TOP_BOTTOM
                row("Average income before taxes", "$84,352", "$87,432", "$94,003", "3.7", "7.5")
                row("Average annual expenditures", "$61,332", "$66,928", "$72,967", "9.1", "9.0")
                row("  Food", "7,310", "8,289", "9,343", "13.4", "12.7")
                row("  Housing", "21,417", "22,624", "24,298", "5.6", "7.4")
                row("  Apparel and services", "1,434", "1,754", "1,945", "22.3", "10.9")
                row("  Transportation", "9,826", "10,961", "12,295", "11.6", "12.2")
                row("  Healthcare", "5,177", "5,452", "5,850", "5.3", "7.3")
                row("  Entertainment", "2,909", "3,568", "3,458", "22.7", "-3.1")
                row("  Education", "1,271", "1,226", "1,335", "-3.5", "8.9")
            }
            footer {
                style(italic = true)
                row {
                    cells("Remaining income", "$23,020", "$20,504", "$21,036")
                }
            }
            captionBottom(TextStyles.dim("via U.S. Bureau of Labor Statistics"))
        }
    )
    if (args.isEmpty()) {
        val choice = t.prompt(
            prompt = "What to run",
            default = "Everything",
            choices = listOf("Everything").plus(years.map { it.toString() })
        )
        if (choice == "Everything") {
            years.forEach {
                run(it)
            }
        } else if (choice?.toIntOrNull() in years) {
            val year = choice!!.toInt()
            val dayChoice = t.prompt(
                prompt = "Year $year",
                default = "Everything",
                choices = listOf("Everything").plus((1..25).map { it.toString() })
            )
            if (dayChoice == "Everything") {
                run(year)
            } else if (dayChoice?.toIntOrNull() in 1..25) {
                val day = dayChoice!!.toInt()
                val sampleChoice = t.prompt(
                    prompt = "Include Samples",
                    default = "Yes",
                    choices = listOf("Yes", "No")
                )
                runDay(
                    year = year,
                    day = day,
                    skipSlow = false,
                    runSamples = sampleChoice == "Yes"
                )
            }
        }
    } else {
        when (args[0]) {
            "-y" -> args.getOrNull(1)?.toIntOrNull()?.takeIf { it in years }?.let { run(it) }
            "-d" -> runDay(year = args[1].toInt(), day = args[2].toInt(), skipSlow = false, runSamples = true)
            "-i" -> runDay(
                year = args[1].drop(1).toInt(),
                day = args[2].drop(3).toInt(),
                skipSlow = false,
                runSamples = true
            )

            else -> return
        }
        println("Correct: $correct ${g("✔")}")
        println("Incorrect: $incorrect ${r("⚠")}")
    }
}

fun run(year: Int) {
    (1..25).forEach {
        try {
            runDay(year, it, skipLongRunning, false)
        } catch (_: ClassNotFoundException) {
        }
    }
}

fun runDay(year: Int, day: Int, skipSlow: Boolean = false, runSamples: Boolean) {
    val d = getDayInstance(year, day)
    println("Year $year Day $day:")

    if (runSamples) {
        val samples = IO.readSamples(year, day)
        print("Samples Part 1:")
        if (samples?.part1.isNullOrEmpty()) {
            println(" [${r("NO DATA")}]")
        } else {
            println()
        }
        samples?.part1?.forEachIndexed { index, sample ->
            print("\t(${index + 1} / ${samples.part1.size}) Init[")
            val init = runInit(d, sample.input.lines())
            print("${init.first.coloredTime()}] - Solve[")
            val result = runPart(d, 1, init.second, sample.solution)
            println("${result.first.coloredTime()}]: ${result.second}")
        }

        print("Samples Part 2:")
        if (samples?.part2.isNullOrEmpty()) {
            println(" [${r("NO DATA")}]")
        } else {
            println()
        }
        samples?.part2?.forEachIndexed { index, sample ->
            print("\t(${index + 1} / ${samples.part2.size}) Init[")
            val init = runInit(d, sample.input.lines())
            print("${init.first.coloredTime()}] - Solve[")
            val result = runPart(d, 2, init.second, sample.solution)
            println("${result.first.coloredTime()}]: ${result.second}")
        }
    }
    var sumTime = 0.0
    val input = IO.readStrings(d.year, d.day)
    if (input.isEmpty()) {
        println("Real Input: [${r("NO DATA")}]")
    } else {
        println("Real Input: ")
        print("\tInit[")
        val init = runInit(d, input)
        println("${init.first.coloredTime()}]")
        sumTime += init.first
        print("\tPart 1[")
        if (skipSlow && year to day to 1 in skips) {
            println("${r("SKIPPED")}]")
        } else {
            val result = runPart(d, 1, init.second, expected[Triple(d.year, d.day, 1)])
            println("${result.first.coloredTime()}]: ${result.second}")
            sumTime += result.first
        }

        print("\tPart 2[")
        if (skipSlow && year to day to 2 in skips) {
            println("${r("SKIPPED")}]")
        } else {
            val result = runPart(d, 2, init.second, expected[Triple(d.year, d.day, 2)])
            println("${result.first.coloredTime()}]: ${result.second}")
            sumTime += result.first
        }

        println("\tTotal [${sumTime.coloredTime()}]")
    }
    println("-".repeat(80))
}

fun runPart(day: Day<Any>, part: Int, input: Any, expected: String?): Pair<Double, String> {
    var result: String
    val time =
        measureNanoTime { result = (if (part == 1) day.solve1(input) else day.solve2(input)).toString() } / 1000000000.0
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    if (isCorrect) correct++ else incorrect++
    val resultString =
        result + (if (isCorrect) "\t${g("SUCCESS")}" else "\t${r("FAILED")}")
    return time to resultString
}

fun runInit(day: Day<Any>, input: List<String>): Pair<Double, Any> {
    var parsed: Any
    val time = measureNanoTime { with(day) { parsed = input.parse() } } / 1000000000.0
    return time to parsed
}

private data class YearResult(val days: List<DayResult>) {
    val totalTime get() = days.sumOf { it.totalTime }
}

private data class DayResult(
    val sampleInitTime: Double,
    val samples: List<PartResult>,
    val part1: PartResult,
    val part2: PartResult,
    val initTime: Double
) {
    val totalTime get() = initTime + part1.time + part2.time
}

private data class PartResult(val success: Boolean, val result: String, val time: Double)

fun parseExpected() = getExpectedLines().associate { line ->
    val split = line.split(":")
    val part = split[0].split(",").map { it.toInt() }
    Triple(part[0], part[1], part[2]) to split[1]
}

fun getExpectedLines(): List<String> {
    Files.lines(Paths.get("src/main/resources/solutions.txt")).use { lines -> return lines.toList() }
}

@Suppress("UNCHECKED_CAST")
private fun getDayInstance(year: Int, day: Int) =
    Class.forName("y$year.Day${day.toString().padStart(2, '0')}")?.getDeclaredConstructor()
        ?.newInstance() as Day<Any>

private fun g(text: String) = "$ANSI_GREEN$text$ANSI_RESET"
private fun r(text: String) = "$ANSI_RED$text$ANSI_RESET"
private fun Double.color() = if (this <= 1) ANSI_GREEN else ANSI_RED
private fun Double.coloredTime() = "${color()}${"%.6f".format(this)}s$ANSI_RESET"
