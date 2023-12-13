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
import expected
import formattedTime
import getDayInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import runInit
import runPart
import runner.DayState
import runner.ResultState
import runner.Sample
import runner.Samples
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
            val state = remember { mutableStateOf(DayState()) }

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
                    state.value = DayState()
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
private fun DayLayout(day: Day<Any>, samples: Samples?, state: MutableState<DayState>, scope: CoroutineScope) =
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { scope.launch { runDayComposeSingle(day, samples, true, state) } }) {
                Icon(Icons.Default.PlayArrow, "")
                Text("Run with samples")
            }
            Button(onClick = { scope.launch { runDayComposeSingle(day, samples, false, state) } }) {
                Icon(Icons.Default.PlayArrow, "")
                Text("Run without samples")
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Year: ")
            TextValue(day.year.toString())
            Label("   Day: ")
            TextValue(day.day.toString())
        }
        if (samples != null) {
            SamplePartLayout(1, samples.part1, state.value.part1SampleTimes, state.value.part1SampleResults)
            SamplePartLayout(2, samples.part2, state.value.part2SampleTimes, state.value.part2SampleResults)
        }

        InitLayout(state.value.initTime)

        PartLayout(1, state.value.part1Time, state.value.part1Result)
        PartLayout(2, state.value.part2Time, state.value.part2Result)
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
private fun TimeValue(time: Double) = TextValue(time.formattedTime(), color = if (time < 1) Color.Green else Color.Red)

@Composable
private fun SamplePartLayout(part: Int, sample: List<Sample>, times: Map<Int, Double>, results: Map<Int, ResultState>) {
    sample.indices.forEach { index ->
        val time = times[index + 1]
        val result = results[index + 1]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Sample $part.${index + 1}: ")
            TimeValue(time ?: 0.0)
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

private suspend fun runDayComposeSingle(
    day: Day<Any>,
    samples: Samples?,
    runSamples: Boolean,
    state: MutableState<DayState>,
) {
    if (runSamples) {
        samples?.part1?.forEachIndexed { index, sample ->
            state.value = state.value.copy(runningPart1Sample = index + 1)
            val startTime = System.nanoTime()
            val init = runInit(day, sample.input.lines())
            val result = runPart(day, 1, init, sample.solution)
            val time = (System.nanoTime() - startTime) / 1000000000.0
            state.value = state.value.apply {
                part1SampleTimes[index + 1] = time
                part1SampleResults[index + 1] = result
            }
        }
        state.value = state.value.copy(runningPart1Sample = null)

        samples?.part2?.forEachIndexed { index, sample ->
            state.value = state.value.copy(runningPart2Sample = index + 1)
            val startTime = System.nanoTime()
            val init = runInit(day, sample.input.lines())
            val result = runPart(day, 2, init, sample.solution)
            val time = (System.nanoTime() - startTime) / 1000000000.0
            state.value = state.value.apply {
                part2SampleTimes[index + 1] = time
                part2SampleResults[index + 1] = result
            }
        }
        state.value = state.value.copy(runningPart2Sample = null)
    }

    val rawInput = IO.readStrings(day.year, day.day)
    if (rawInput.any { it.isNotBlank() }) {
        state.value = state.value.copy(runningInit = true)
        val initStartTime = System.nanoTime()
        val input = runInit(day, rawInput)
        val initTime = (System.nanoTime() - initStartTime) / 1000000000.0

        state.value = state.value.copy(runningInit = false, initTime = initTime, runningPart1 = true)

        val part1StartTime = System.nanoTime()
        val part1Result = runPart(day, 1, input, expected[Triple(day.year, day.day, 1)])
        val part1Time = (System.nanoTime() - part1StartTime) / 1000000000.0

        state.value = state.value.copy(
            runningPart1 = false,
            part1Time = part1Time,
            part1Result = part1Result,
            runningPart2 = true
        )

        val part2StartTime = System.nanoTime()
        val part2Result = runPart(day, 2, input, expected[Triple(day.year, day.day, 2)])
        val part2Time = (System.nanoTime() - part2StartTime) / 1000000000.0
        state.value = state.value.copy(runningPart2 = false, part2Time = part2Time, part2Result = part2Result)
    }
}
