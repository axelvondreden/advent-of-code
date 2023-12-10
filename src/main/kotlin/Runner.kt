import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.IO

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

const val skipLongRunning = true

val expected = parseExpected()

val t = Terminal()

@Composable
@Preview
fun App() {
    MaterialTheme(colors = darkColors()) {
        Column(modifier = Modifier.background(Color(0xFF121212)).fillMaxSize()) {
            var text by remember { mutableStateOf("Hello, World!") }
            Button(onClick = {
                text = "Hello, Desktop!"
            }) {
                Text(text)
            }
        }
    }
}


/**
 * [no args]: run compose app
 * -y $year: run all for year
 * -d $year $day: run single day
 * -i $FilePackage$ $FileNameWithoutAllExtensions$: IntelliJ program args for running current File
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        application {
            Window(onCloseRequest = ::exitApplication) {
                App()
            }
        }
    } else {
        when (args[0]) {
            "-y" -> args.getOrNull(1)?.toIntOrNull()?.takeIf { it in years }?.let { run(it) }
            "-d" -> runDaySingle(year = args[1].toInt(), day = args[2].toInt(), runSamples = true)
            "-i" -> runDaySingle(
                year = args[1].drop(1).toInt(),
                day = args[2].drop(3).toInt(),
                runSamples = true
            )

            else -> return
        }
    }
}

fun run(year: Int) {
    (1..25).forEach {
        try {
            runDaySingle(year, it, false)
        } catch (_: ClassNotFoundException) {
        }
    }
}

fun runDaySingle(year: Int, day: Int, runSamples: Boolean) {
    t.println("Year ${(black on white)(year.toString())} Day ${(black on white)(day.toString())}")

    val d = getDayInstance(year, day)
    val samples = IO.readSamples(year, day)

    if (runSamples) {
        t.print("Samples Part 1:")
        if (samples?.part1.isNullOrEmpty()) {
            t.println(" ${(white on brightRed)("NO DATA")}")
        } else {
            t.println()
        }
        samples?.part1?.forEachIndexed { index, sample ->
            t.print("\t${index + 1}/${samples.part1.size}:\t")
            val startTime = System.nanoTime()
            val init = runInit(d, sample.input.lines())
            val result = runPart(d, 1, init, sample.solution)
            val time = (System.nanoTime() - startTime) / 1000000000.0
            t.println("${time.coloredTime()}\tResult: ${
                if (result.correct) (black on brightGreen)("CORRECT") else (white on brightRed)("FAILED")
            } ${(black on white)(result.result)}")
        }
        t.println()

        t.print("Samples Part 2:")
        if (samples?.part2.isNullOrEmpty()) {
            t.println(" ${(white on brightRed)("NO DATA")}")
        } else {
            t.println()
        }
        samples?.part2?.forEachIndexed { index, sample ->
            t.print("\t${index + 1}/${samples.part1.size}:\t")
            val startTime = System.nanoTime()
            val init = runInit(d, sample.input.lines())
            val result = runPart(d, 2, init, sample.solution)
            val time = (System.nanoTime() - startTime) / 1000000000.0
            t.println("${time.coloredTime()}\tResult: ${
                if (result.correct) (black on brightGreen)("CORRECT") else (white on brightRed)("FAILED")
            } ${(black on white)(result.result)}")
        }
        t.println()
    }

    val rawInput = IO.readStrings(d.year, d.day)
    if (rawInput.isNotEmpty()) {
        runBlocking {
            t.println("Actual Input:")
            val initStartTime = System.nanoTime()
            val init = async {
                runInit(d, rawInput)
            }
            launch {
                while (init.isActive) {
                    /*state.initTime = (System.nanoTime() - initStartTime) / 1000000000.0
                    a.update(state)
                    delay(100)*/
                }
            }
            val input = init.await()
            val initTime = (System.nanoTime() - initStartTime) / 1000000000.0
            t.println("\tInit:\t${initTime.coloredTime()}")

            val part1StartTime = System.nanoTime()
            val part1 = async {
                runPart(d, 1, input, expected[Triple(d.year, d.day, 1)])
            }
            launch {
                while (part1.isActive) {
                    /*state.part1Time = (System.nanoTime() - part1StartTime) / 1000000000.0
                    a.update(state)
                    delay(100)*/
                }
            }
            val part1Result = part1.await()
            val part1Time = (System.nanoTime() - part1StartTime) / 1000000000.0
            t.println(
                "\tPart 1:\t${part1Time.coloredTime()}\tResult: ${
                    if (part1Result.correct) (black on brightGreen)("CORRECT") else (white on brightRed)("FAILED")
                } ${(black on white)(part1Result.result)}"
            )

            val part2StartTime = System.nanoTime()
            val part2 = async {
                runPart(d, 2, input, expected[Triple(d.year, d.day, 2)])
            }
            launch {
                while (part2.isActive) {
                    /*state.part2Time = (System.nanoTime() - part2StartTime) / 1000000000.0
                    a.update(state)
                    delay(100)*/
                }
            }
            val part2Result = part2.await()
            val part2Time = (System.nanoTime() - part2StartTime) / 1000000000.0
            t.println(
                "\tPart 2:\t${part2Time.coloredTime()}\tResult: ${
                    if (part2Result.correct) (black on brightGreen)("CORRECT") else (white on brightRed)("FAILED")
                } ${(black on white)(part2Result.result)}"
            )
            t.println("\tTotal:\t${(initTime + part1Time + part2Time).coloredTime()}")
        }
    }
}

private fun runPart(day: Day<Any>, part: Int, input: Any, expected: String?): ResultState {
    val result = (if (part == 1) day.solve1(input) else day.solve2(input)).toString()
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    return ResultState(result, isCorrect)
}

fun runInit(day: Day<Any>, input: List<String>) = with(day) { return@with input.parse() }

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

private data class DayState(
    var runningInit: Boolean = false,
    var runningPart1: Boolean = false,
    var runningPart2: Boolean = false,
    var runningPart1Sample: Int? = null,
    var runningPart2Sample: Int? = null,
    var initTime: Double? = null,
    var part1Time: Double? = null,
    var part2Time: Double? = null,
    val part1SampleTimes: MutableMap<Int, Double> = mutableMapOf(),
    val part2SampleTimes: MutableMap<Int, Double> = mutableMapOf(),
    var part1Result: ResultState? = null,
    var part2Result: ResultState? = null,
    val part1SampleResults: MutableMap<Int, ResultState> = mutableMapOf(),
    val part2SampleResults: MutableMap<Int, ResultState> = mutableMapOf()
)

private data class ResultState(val result: String, val correct: Boolean)

fun parseExpected() = getExpectedLines().associate { line ->
    val split = line.split(":")
    val part = split[0].split(",").map { it.toInt() }
    Triple(part[0], part[1], part[2]) to split[1]
}

fun getExpectedLines(): List<String> {
    return IO::class.java.classLoader.getResource("solutions.txt")!!.readText().split("\r\n", "\n")
}

@Suppress("UNCHECKED_CAST")
private fun getDayInstance(year: Int, day: Int) =
    Class.forName("y$year.Day${day.toString().padStart(2, '0')}")?.getDeclaredConstructor()
        ?.newInstance() as Day<Any>

private fun Double.coloredTime() = if (this < 1) green("${"%.6f".format(this)}s") else red("${"%.6f".format(this)}s")
