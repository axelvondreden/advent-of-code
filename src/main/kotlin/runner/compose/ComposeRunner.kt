package runner.compose

import Day
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import expected
import formattedTime
import getDayInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import runInit
import runPart
import runner.*
import runner.Target
import utils.IO
import years
import kotlin.math.max
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.reflect

@Composable
@Preview
fun App() {
    MaterialTheme(colors = darkColors(primary = Color.Cyan)) {
        val scope = rememberCoroutineScope()
        Column(modifier = Modifier.background(Color(0xFF121212)).fillMaxSize()) {
            var selectedYear by remember { mutableStateOf<Int?>(null) }
            var selectedDay by remember { mutableStateOf<Day<Any>?>(null) }
            val availableDays = remember { mutableStateListOf<Day<Any>>() }
            var allOfYearSelected by remember { mutableStateOf(false) }
            val dayState = DayState(
                initStartTime = remember { mutableStateOf(0L) },
                initTime = remember { mutableStateOf(0L) },
                part1StartTime = remember { mutableStateOf(0L) },
                part1Time = remember { mutableStateOf(0L) },
                part2StartTime = remember { mutableStateOf(0L) },
                part2Time = remember { mutableStateOf(0L) },
                part1Result = remember { mutableStateOf(null) },
                part2Result = remember { mutableStateOf(null) },
                part1SampleTimes = remember { mutableStateMapOf() },
                part2SampleTimes = remember { mutableStateMapOf() },
                part1SampleResults = remember { mutableStateMapOf() },
                part2SampleResults = remember { mutableStateMapOf() },
                target = remember { mutableStateOf(null) }
            )
            val yearState = YearState(
                initTimes = remember { mutableStateMapOf() },
                part1Times = remember { mutableStateMapOf() },
                part2Times = remember { mutableStateMapOf() },
                part1Results = remember { mutableStateMapOf() },
                part2Results = remember { mutableStateMapOf() },
                target = remember { mutableStateOf(null) }
            )

            YearSelect(
                onAllClick = {},
                selectedYear = selectedYear,
                onYearSelect = { year ->
                    allOfYearSelected = false
                    selectedDay = null
                    selectedYear = year
                    availableDays.clear()
                    scope.launch {
                        (1..25).forEach { day ->
                            getDayInstance(year, day)?.let { availableDays.add(it) }
                        }
                    }
                }
            )
            if (selectedYear != null) {
                DaySelect(
                    onAllClick = {
                        yearState.reset()
                        selectedDay = null
                        allOfYearSelected = true
                    },
                    days = availableDays,
                    selectedDay = selectedDay?.day,
                    allSelected = allOfYearSelected,
                    onDaySelect = {
                        allOfYearSelected = false
                        dayState.reset()
                        selectedDay = it
                    })

                val day = selectedDay
                if (day != null) {
                    val samples = IO.readSamples(day.year, day.day)
                    DayLayout(day, samples, dayState, scope)
                } else if (allOfYearSelected) {
                    YearLayout(selectedYear!!, availableDays, yearState, scope)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun YearSelect(onAllClick: () -> Unit, selectedYear: Int?, onYearSelect: (Int) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { onAllClick() }) {
            Text("All")
        }
        years.forEach { year ->
            Button(
                onClick = { if (year != selectedYear) onYearSelect(year) },
                colors = if (year == selectedYear) ButtonDefaults.buttonColors(backgroundColor = Color.Green) else ButtonDefaults.buttonColors()
            ) {
                Text(year.toString())
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DaySelect(
    onAllClick: () -> Unit,
    days: List<Day<Any>>,
    selectedDay: Int?,
    allSelected: Boolean,
    onDaySelect: (Day<Any>) -> Unit
) {
    FlowRow(modifier = Modifier.fillMaxWidth().border(1.dp, Color.White)) {
        Button(
            onClick = { onAllClick() },
            colors = if (allSelected) ButtonDefaults.buttonColors(backgroundColor = Color.Green) else ButtonDefaults.buttonColors()
        ) {
            Text("All")
        }
        days.forEach { day ->
            Button(
                onClick = { if (day.day != selectedDay) onDaySelect(day) },
                colors = if (day.day == selectedDay) ButtonDefaults.buttonColors(backgroundColor = Color.Green) else ButtonDefaults.buttonColors()
            ) {
                Text(day.day.toString())
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun YearLayout(year: Int, days: List<Day<Any>>, state: YearState, scope: CoroutineScope) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                scope.launch(Dispatchers.IO) {
                    runDays(
                        days = days,
                        onInitStart = { day, time ->
                            state.initTimes[day] = time to 0L
                            state.target.value = YearTarget.Init(day)
                        },
                        onInitEnd = { day, time ->
                            val start = state.initTimes[day]!!.first
                            state.initTimes[day] = start to time
                            state.target.value = null
                        },
                        onPart1Start = { day, time ->
                            state.part1Times[day] = time to 0L
                            state.target.value = YearTarget.Part1(day)
                        },
                        onPart1End = { day, time, result ->
                            val start = state.part1Times[day]!!.first
                            state.part1Times[day] = start to time
                            state.part1Results[day] = result
                            state.target.value = null
                        },
                        onPart2Start = { day, time ->
                            state.part2Times[day] = time to 0L
                            state.target.value = YearTarget.Part2(day)
                        },
                        onPart2End = { day, time, result ->
                            val start = state.part2Times[day]!!.first
                            state.part2Times[day] = start to time
                            state.part2Results[day] = result
                            state.target.value = null
                        }
                    )
                }
            }) {
            Icon(Icons.Default.PlayArrow, "")
            Text("Run All")
        }
        LaunchedEffect(state.target.value) {
            while (state.target.value is YearTarget.Init) {
                state.target.value?.day?.let { day ->
                    state.initTimes[day]?.first?.let { start ->
                        state.initTimes[day] = start to System.nanoTime()
                    }
                }
                delay(100)
            }
        }
        LaunchedEffect(state.target.value) {
            while (state.target.value is YearTarget.Part1) {
                state.target.value?.day?.let { day ->
                    state.part1Times[day]?.first?.let { start ->
                        state.part1Times[day] = start to System.nanoTime()
                    }
                }
                delay(100)
            }
        }
        LaunchedEffect(state.target.value) {
            while (state.target.value is YearTarget.Part2) {
                state.target.value?.day?.let { day ->
                    state.part2Times[day]?.first?.let { start ->
                        state.part2Times[day] = start to System.nanoTime()
                    }
                }
                delay(100)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Year: ")
            TextValue(year.toString())
            Spacer(Modifier.width(10.dp))
            Label("Time Total: ")
            val totalTime = state.initTimes.values.sumOf { max(0.0, (it.second - it.first) / 1000000000.0) } +
                state.part1Times.values.sumOf { max(0.0, (it.second - it.first) / 1000000000.0) } +
                state.part2Times.values.sumOf { max(0.0, (it.second - it.first) / 1000000000.0) }
            val size = days.size.toDouble()
            TimeValue(
                time = totalTime,
                colorRange = listOf(size / 10, size / 2, size)
            )
            Spacer(Modifier.width(10.dp))
            Label("Time / Day: ")
            TimeValue(time = totalTime / days.size)
            Spacer(Modifier.width(10.dp))
            Label("Correct: ")
            TextValue(
                text = (state.part1Results.values.count { it.correct } + state.part2Results.values.count { it.correct }).toString(),
                color = Color.Green
            )
            Spacer(Modifier.width(4.dp))
            Label("Incorrect: ")
            TextValue(
                text = (state.part1Results.values.count { !it.correct } + state.part2Results.values.count { !it.correct }).toString(),
                color = Color.Red
            )
        }
        FlowRow {
            days.forEach { day ->
                DayLayoutCompact(
                    day = day,
                    target = state.target.value,
                    initTime = state.initTimes[day.day]?.let { it.second - it.first },
                    part1Time = state.part1Times[day.day]?.let { it.second - it.first },
                    part2Time = state.part2Times[day.day]?.let { it.second - it.first },
                    part1Result = state.part1Results[day.day],
                    part2Result = state.part2Results[day.day]
                )
            }
        }
    }
}

@Composable
private fun DayLayoutCompact(
    day: Day<Any>,
    target: YearTarget?,
    initTime: Long?,
    part1Time: Long?,
    part2Time: Long?,
    part1Result: ResultState?,
    part2Result: ResultState?
) {
    Box(modifier = Modifier.border(1.dp, Color.White).padding(2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.background(if (target?.day == day.day) Color.DarkGray else MaterialTheme.colors.background)) {
                Label("Day ${day.day}", minWidth = 60.dp)
            }
            Column {
                InitLayoutCompact(initTime?.div(1000000000.0), target is YearTarget.Init && target.day == day.day)
                PartLayoutCompact(
                    1,
                    part1Time?.div(1000000000.0),
                    part1Result,
                    target is YearTarget.Part1 && target.day == day.day
                )
                PartLayoutCompact(
                    2,
                    part2Time?.div(1000000000.0),
                    part2Result,
                    target is YearTarget.Part2 && target.day == day.day
                )
            }
        }
    }
}

@Composable
private fun DayLayout(day: Day<Any>, samples: Samples?, state: DayState, scope: CoroutineScope) {
    var visualizationData by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DaySingleButton(day, samples, state, scope)
            val caller1 = day::class.memberFunctions.first { it.name == "solve1Visualized" }.javaMethod!!.declaringClass
            val caller2 = day::class.memberFunctions.first { it.name == "solve2Visualized" }.javaMethod!!.declaringClass
            Button(onClick = {}, enabled = caller1.name != "Day") {
                Icon(Icons.Default.Star, "")
                Text("Visualize Part 1")
            }
            Button(onClick = {}, enabled = caller2.name != "Day") {
                Icon(Icons.Default.Star, "")
                Text("Visualize Part 2")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Year: ")
            TextValue(day.year.toString())
            Label("   Day: ")
            TextValue(day.day.toString())
        }
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.width(400.dp).border(1.dp, Color.LightGray)) {
                if (samples != null) {
                    LaunchedEffect(state.target.value) {
                        while (state.target.value is Target.Sample1) {
                            delay(100)
                            val nr = state.target.value?.nr
                            if (nr != null) {
                                val start = state.part1SampleTimes[nr]?.first
                                if (start != null) {
                                    state.part1SampleTimes[nr] = start to System.nanoTime()
                                }
                            }
                        }
                    }
                    SamplePartLayout(1, samples.part1, state.part1SampleTimes, state.part1SampleResults)

                    LaunchedEffect(state.target.value) {
                        while (state.target.value is Target.Sample2) {
                            delay(100)
                            val nr = state.target.value?.nr
                            if (nr != null) {
                                val start = state.part2SampleTimes[nr]?.first
                                if (start != null) {
                                    state.part2SampleTimes[nr] = start to System.nanoTime()
                                }
                            }
                        }
                    }
                    SamplePartLayout(2, samples.part2, state.part2SampleTimes, state.part2SampleResults)
                }

                LaunchedEffect(state.target.value) {
                    while (state.target.value == Target.Init) {
                        delay(100)
                        state.initTime.value = System.nanoTime() - state.initStartTime.value
                    }
                }
                InitLayout(state.initTime.value / 1000000000.0)

                LaunchedEffect(state.target.value) {
                    while (state.target.value is Target.Part && state.target.value?.nr == 1) {
                        delay(100)
                        state.part1Time.value = System.nanoTime() - state.part1StartTime.value
                    }
                }
                PartLayout(1, state.part1Time.value / 1000000000.0, state.part1Result.value)

                LaunchedEffect(state.part2StartTime.value) {
                    while (state.target.value is Target.Part && state.target.value?.nr == 2) {
                        delay(100)
                        state.part2Time.value = System.nanoTime() - state.part2StartTime.value
                    }
                }
                PartLayout(2, state.part2Time.value / 1000000000.0, state.part2Result.value)
            }
            Column(Modifier.fillMaxHeight()) {
                Label("Visualized:")
                Box(Modifier.fillMaxSize().border(1.dp, Color.LightGray)) {
                    Text(visualizationData)
                }
            }
        }
    }
}

@Composable
private fun DaySingleButton(day: Day<Any>, samples: Samples?, state: DayState, scope: CoroutineScope) {
    Button(
        onClick = {
            state.reset()
            scope.launch(Dispatchers.IO) {
                runSingleDay(
                    day = day,
                    samples = samples,
                    onInitStart = {
                        state.initStartTime.value = it
                        state.target.value = Target.Init
                    },
                    onInitEnd = {
                        state.initTime.value = System.nanoTime() - state.initStartTime.value
                        state.target.value = null
                    },
                    onPart1Start = {
                        state.part1StartTime.value = it
                        state.target.value = Target.Part(1)
                    },
                    onPart1End = { time, result ->
                        state.part1Time.value = time - state.part1StartTime.value
                        state.part1Result.value = result
                        state.target.value = null
                    },
                    onPart2Start = {
                        state.part2StartTime.value = it
                        state.target.value = Target.Part(2)
                    },
                    onPart2End = { time, result ->
                        state.part2Result.value = result
                        state.part2Time.value = time - state.part2StartTime.value
                        state.target.value = null
                    },
                    onSampleStart = { part, nr, time ->
                        if (part == 1) {
                            state.part1SampleTimes[nr] = time to 0L
                            state.target.value = Target.Sample1(nr)
                        } else {
                            state.part2SampleTimes[nr] = time to 0L
                            state.target.value = Target.Sample2(nr)
                        }
                    },
                    onSampleEnd = { part, nr, time, result ->
                        if (part == 1) {
                            val start = state.part1SampleTimes[nr]!!.first
                            state.part1SampleTimes[nr] = start to time
                            state.part1SampleResults[nr] = result
                        } else {
                            val start = state.part2SampleTimes[nr]!!.first
                            state.part2SampleTimes[nr] = start to time
                            state.part2SampleResults[nr] = result
                        }
                        state.target.value = null
                    }
                )
            }
        }) {
        Icon(Icons.Default.PlayArrow, "")
        Text("Run")
    }
}

@Composable
private fun PartLayout(part: Int, time: Double?, result: ResultState?) =
    Row(verticalAlignment = Alignment.CenterVertically) {
        Label("Part $part: ", minWidth = 120.dp)
        TimeValue(time ?: 0.0)
        ResultIcon(result)
        Spacer(Modifier.width(2.dp))
        result?.result?.let { TextValue(it) }
    }

@Composable
private fun PartLayoutCompact(part: Int, time: Double?, result: ResultState?, active: Boolean) = Row(
    modifier = if (active) Modifier.background(Color.DarkGray) else Modifier,
    verticalAlignment = Alignment.CenterVertically
) {
    Label("Part $part: ", minWidth = 100.dp)
    TimeValue(time ?: 0.0)
    ResultIcon(result)
}

@Composable
private fun ResultIcon(result: ResultState?) {
    Spacer(Modifier.width(2.dp))
    when (result?.correct) {
        true -> Icon(Icons.Default.Check, "", tint = Color.Green)
        false -> Icon(Icons.Default.Close, "", tint = Color.Red)
        else -> Icon(Icons.Default.Build, "")
    }
}

@Composable
private fun TimeValue(time: Double, colorRange: List<Double> = listOf(0.1, 0.5, 1.0)) {
    val posTime = time.coerceAtLeast(0.0)
    TextValue(posTime.formattedTime(), color = colorRange.getColor(posTime))
}

private fun List<Double>.getColor(value: Double) = when {
    value < this[0] -> Color.Green
    value < this[1] -> Color.Yellow
    value < this[2] -> Color(252, 145, 5)
    else -> Color.Red
}

@Composable
private fun SamplePartLayout(
    part: Int,
    sample: List<Sample>,
    times: Map<Int, Pair<Long, Long>>,
    results: Map<Int, ResultState>
) {
    sample.indices.forEach { index ->
        val pair = times[index + 1]
        val time = pair?.let { it.second - it.first }
        val result = results[index + 1]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Sample $part.${index + 1}: ")
            TimeValue(time?.div(1000000000.0) ?: 0.0)
            ResultIcon(result)
            Spacer(Modifier.width(2.dp))
            result?.result?.let { TextValue(it) }
        }
    }
}

@Composable
private fun InitLayout(time: Double?) = Row(verticalAlignment = Alignment.CenterVertically) {
    Label("Init: ", minWidth = 120.dp)
    TimeValue(time ?: 0.0)
}

@Composable
private fun InitLayoutCompact(time: Double?, active: Boolean) = Row(
    modifier = if (active) Modifier.background(Color.DarkGray) else Modifier,
    verticalAlignment = Alignment.CenterVertically
) {
    Label("Init: ", minWidth = 100.dp)
    TimeValue(time ?: 0.0)
}

@Composable
private fun TextValue(text: String, color: Color = MaterialTheme.colors.onBackground) =
    Box(modifier = Modifier.border(1.dp, Color.Gray), contentAlignment = Alignment.Center) {
        Text(text, modifier = Modifier.padding(4.dp), color = color, fontFamily = FontFamily.Monospace)
    }

@Composable
private fun Label(text: String, color: Color = MaterialTheme.colors.onBackground, minWidth: Dp = Dp.Unspecified) =
    Text(text = text, modifier = Modifier.padding(4.dp).widthIn(min = minWidth), color = color)

private fun runSingleDay(
    day: Day<Any>,
    samples: Samples?,
    onInitStart: (Long) -> Unit,
    onInitEnd: (Long) -> Unit,
    onPart1Start: (Long) -> Unit,
    onPart1End: (Long, ResultState) -> Unit,
    onPart2Start: (Long) -> Unit,
    onPart2End: (Long, ResultState) -> Unit,
    onSampleStart: (Int, Int, Long) -> Unit,
    onSampleEnd: (Int, Int, Long, ResultState) -> Unit
) {
    if (samples != null) {
        samples.part1.forEachIndexed { index, sample ->
            onSampleStart(1, index + 1, System.nanoTime())
            val init = runInit(day, sample.input.lines())
            val result = runPart(day, 1, init, sample.solution)
            onSampleEnd(1, index + 1, System.nanoTime(), result)
        }

        samples.part2.forEachIndexed { index, sample ->
            onSampleStart(2, index + 1, System.nanoTime())
            val init = runInit(day, sample.input.lines())
            val result = runPart(day, 2, init, sample.solution)
            onSampleEnd(2, index + 1, System.nanoTime(), result)
        }
    }
    val rawInput = IO.readStrings(day.year, day.day)
    if (rawInput.any { it.isNotBlank() }) {
        onInitStart(System.nanoTime())
        val input = runInit(day, rawInput)
        onInitEnd(System.nanoTime())

        onPart1Start(System.nanoTime())
        val part1Result = runPart(day, 1, input, expected[Triple(day.year, day.day, 1)])
        onPart1End(System.nanoTime(), part1Result)

        onPart2Start(System.nanoTime())
        val part2Result = runPart(day, 2, input, expected[Triple(day.year, day.day, 2)])
        onPart2End(System.nanoTime(), part2Result)
    }
}

private suspend fun runSingleDayVisualized(
    day: Day<Any>,
    onInitStart: (Long) -> Unit,
    onInitEnd: (Long) -> Unit,
    onPart1Start: (Long) -> Unit,
    onPart1Progress: (String?) -> Unit,
    onPart1End: (Long, ResultState) -> Unit,
    onPart2Start: (Long) -> Unit,
    onPart2Progress: (String?) -> Unit,
    onPart2End: (Long, ResultState) -> Unit
) {
    val rawInput = IO.readStrings(day.year, day.day)
    if (rawInput.any { it.isNotBlank() }) {
        onInitStart(System.nanoTime())
        val input = runInit(day, rawInput)
        onInitEnd(System.nanoTime())

        onPart1Start(System.nanoTime())
        val result1 = runPart1WithVisualization(day, input, expected[Triple(day.year, day.day, 1)], onPart1Progress)
        onPart1End(System.nanoTime(), result1)

        onPart2Start(System.nanoTime())
        val result2 = runPart2WithVisualization(day, input, expected[Triple(day.year, day.day, 2)], onPart2Progress)
        onPart2End(System.nanoTime(), result2)
    }
}

private fun runDays(
    days: List<Day<Any>>,
    onInitStart: (Int, Long) -> Unit,
    onInitEnd: (Int, Long) -> Unit,
    onPart1Start: (Int, Long) -> Unit,
    onPart1End: (Int, Long, ResultState) -> Unit,
    onPart2Start: (Int, Long) -> Unit,
    onPart2End: (Int, Long, ResultState) -> Unit
) {
    days.forEach { day ->
        val rawInput = IO.readStrings(day.year, day.day)
        if (rawInput.any { it.isNotBlank() }) {
            onInitStart(day.day, System.nanoTime())
            val input = runInit(day, rawInput)
            onInitEnd(day.day, System.nanoTime())

            onPart1Start(day.day, System.nanoTime())
            val part1Result = runPart(day, 1, input, expected[Triple(day.year, day.day, 1)])
            onPart1End(day.day, System.nanoTime(), part1Result)

            onPart2Start(day.day, System.nanoTime())
            val part2Result = runPart(day, 2, input, expected[Triple(day.year, day.day, 2)])
            onPart2End(day.day, System.nanoTime(), part2Result)
        }
    }
}

suspend fun runPart1WithVisualization(
    day: Day<Any>,
    input: Any,
    expected: String?,
    onProgress: (String?) -> Unit
): ResultState {
    val result = day.visualize1(input, onProgress = onProgress, awaitSignal = { delay(1000) }).toString()
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    return ResultState(result, isCorrect)
}

suspend fun runPart2WithVisualization(
    day: Day<Any>,
    input: Any,
    expected: String?,
    onProgress: (String?) -> Unit
): ResultState {
    val result = day.visualize2(input, onProgress = onProgress, awaitSignal = { delay(1000) }).toString()
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    return ResultState(result, isCorrect)
}