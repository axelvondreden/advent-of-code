package runner.compose

import Day
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
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

@Composable
@Preview
fun App() {
    MaterialTheme(colors = darkColors(primary = Color.Cyan)) {
        val scope = rememberCoroutineScope()
        Column(modifier = Modifier.background(Color(0xFF121212)).fillMaxSize()) {
            var selectedYear by remember { mutableStateOf<Int?>(null) }
            var selectedDay by remember { mutableStateOf<Day<Any>?>(null) }
            val availableDays = remember { mutableStateListOf<Day<Any>>() }
            val state = DayState(
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

            YearSelect(
                onAllClick = {},
                selectedYear = selectedYear,
                onYearSelect = { year ->
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
                DaySelect(onAllClick = {}, availableDays, selectedDay?.day, onDaySelect = {
                    state.reset()
                    selectedDay = it
                })

                val day = selectedDay
                if (day != null) {
                    val samples = IO.readSamples(day.year, day.day)
                    DayLayout(day, samples, state, scope)
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
    onDaySelect: (Day<Any>) -> Unit
) {
    FlowRow(modifier = Modifier.fillMaxWidth().border(1.dp, Color.White)) {
        Button(onClick = { onAllClick() }) {
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

@Composable
private fun DayLayout(day: Day<Any>, samples: Samples?, state: DayState, scope: CoroutineScope) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                Text("Run with samples")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Year: ")
            TextValue(day.year.toString())
            Label("   Day: ")
            TextValue(day.day.toString())
        }
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
}

@Composable
private fun PartLayout(part: Int, time: Double?, result: ResultState?) =
    Row(verticalAlignment = Alignment.CenterVertically) {
        Label("Part $part: ")
        TimeValue(time ?: 0.0)
        ResultIcon(result)
        Spacer(Modifier.width(2.dp))
        TextValue(result?.result ?: "")
    }

@Composable
private fun ResultIcon(result: ResultState?) {
    Spacer(Modifier.width(2.dp))
    result?.correct?.let {
        if (it) Icon(Icons.Default.Check, "", tint = Color.Green)
        else Icon(Icons.Default.Close, "", tint = Color.Red)
    }
}

@Composable
private fun TimeValue(time: Double) {
    val posTime = time.coerceAtLeast(0.0)
    TextValue(posTime.formattedTime(), color = if (posTime < 1) Color.Green else Color.Red)
}

@Composable
private fun SamplePartLayout(part: Int, sample: List<Sample>, times: Map<Int, Pair<Long, Long>>, results: Map<Int, ResultState>) {
    sample.indices.forEach { index ->
        val pair = times[index + 1]
        val time = pair?.let { it.second - it.first }
        val result = results[index + 1]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Sample $part.${index + 1}: ")
            TimeValue(time?.div(1000000000.0) ?: 0.0)
            ResultIcon(result)
            Spacer(Modifier.width(2.dp))
            TextValue(result?.result ?: "")
        }
    }
}

@Composable
private fun InitLayout(time: Double?) = Row(verticalAlignment = Alignment.CenterVertically) {
    Label("Init: ")
    TimeValue(time ?: 0.0)
}

@Composable
private fun TextValue(text: String, color: Color = MaterialTheme.colors.onBackground) =
    Box(modifier = Modifier.border(1.dp, Color.Gray), contentAlignment = Alignment.Center) {
        Text(text, modifier = Modifier.padding(4.dp), color = color, fontFamily = FontFamily.Monospace)
    }

@Composable
private fun Label(text: String, color: Color = MaterialTheme.colors.onBackground) =
    Text(text = text, modifier = Modifier.padding(4.dp), color = color)

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