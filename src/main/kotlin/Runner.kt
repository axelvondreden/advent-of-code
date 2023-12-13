import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import runner.ResultState
import runner.compose.App
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
            "-d" -> runDayCmdSingle(year = args[1].toInt(), day = args[2].toInt(), runSamples = true)
            "-i" -> runDayCmdSingle(
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
            runDayCmdSingle(year, it, false)
        } catch (_: ClassNotFoundException) {
        }
    }
}

fun runDayCmdSingle(year: Int, day: Int, runSamples: Boolean) {
    t.println("Year ${(black on white)(year.toString())} Day ${(black on white)(day.toString())}")

    val d = getDayInstance(year, day)!!
    val samples = IO.readSamples(year, day)

    if (runSamples) {
        t.print("Samples Part 1:")
        if (samples?.part1.isNullOrEmpty()) {
            t.println(" ${(black on brightRed)("NO DATA")}")
        } else {
            t.println()
        }
        samples?.part1?.forEachIndexed { index, sample ->
            t.print("\t${index + 1}/${samples.part1.size}:\t")
            val startTime = System.nanoTime()
            val init = runInit(d, sample.input.lines())
            val result = runPart(d, 1, init, sample.solution)
            val time = (System.nanoTime() - startTime) / 1000000000.0
            t.println(
                "${time.coloredTime()}\tResult: ${
                    if (result.correct) (black on brightGreen)("CORRECT") else (black on brightRed)("FAILED")
                } ${(black on white)(result.result)}"
            )
        }
        t.println()

        t.print("Samples Part 2:")
        if (samples?.part2.isNullOrEmpty()) {
            t.println(" ${(black on brightRed)("NO DATA")}")
        } else {
            t.println()
        }
        samples?.part2?.forEachIndexed { index, sample ->
            t.print("\t${index + 1}/${samples.part1.size}:\t")
            val startTime = System.nanoTime()
            val init = runInit(d, sample.input.lines())
            val result = runPart(d, 2, init, sample.solution)
            val time = (System.nanoTime() - startTime) / 1000000000.0
            t.println(
                "${time.coloredTime()}\tResult: ${
                    if (result.correct) (black on brightGreen)("CORRECT") else (black on brightRed)("FAILED")
                } ${(black on white)(result.result)}"
            )
        }
        t.println()
    }

    val rawInput = IO.readStrings(d.year, d.day)
    if (rawInput.any { it.isNotBlank() }) {
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
                    if (part1Result.correct) (black on brightGreen)("CORRECT") else (black on brightRed)("FAILED ")
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
                    if (part2Result.correct) (black on brightGreen)("CORRECT") else (black on brightRed)("FAILED ")
                } ${(black on white)(part2Result.result)}"
            )
            t.println("\tTotal:\t${(initTime + part1Time + part2Time).coloredTime()}")
        }
    }
}

fun runPart(day: Day<Any>, part: Int, input: Any, expected: String?): ResultState {
    val result = (if (part == 1) day.solve1(input) else day.solve2(input)).toString()
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    return ResultState(result, isCorrect)
}

fun runInit(day: Day<Any>, input: List<String>) = with(day) { return@with input.parse() }

fun parseExpected() = getExpectedLines().associate { line ->
    val split = line.split(":")
    val part = split[0].split(",").map { it.toInt() }
    Triple(part[0], part[1], part[2]) to split[1]
}

fun getExpectedLines(): List<String> {
    return IO::class.java.classLoader.getResource("solutions.txt")!!.readText().split("\r\n", "\n")
}

@Suppress("UNCHECKED_CAST")
fun getDayInstance(year: Int, day: Int) = try {
    Class.forName("y$year.Day${day.toString().padStart(2, '0')}")?.getDeclaredConstructor()
        ?.newInstance() as Day<Any>?
} catch (e: Exception) {
    null
}

fun Double.coloredTime() = if (this < 1) green(formattedTime()) else red(formattedTime())

fun Double.formattedTime() = "${"%.${7 - (toInt().toString().length)}f".format(this)}s"