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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import utils.IO

@Composable
@Preview
fun App() {
    MaterialTheme(colors = darkColors()) {
        Column(modifier = Modifier.background(Color(0xFF121212)).fillMaxSize()) {
            var selectedYear by remember { mutableStateOf<Int?>(null) }
            var selectedDay by remember { mutableStateOf<Day<Any>?>(null) }
            val state = remember { mutableStateOf(DayState()) }
            Row(
                modifier = Modifier.fillMaxWidth().border(1.dp, Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (selectedYear == null) {
                    Button(onClick = {}) {
                        Text("All")
                    }
                    years.forEach { year ->
                        Button(onClick = { selectedYear = year }) {
                            Text(year.toString())
                        }
                    }
                } else {
                    Button(onClick = { selectedYear = null; selectedDay = null }) {
                        Text("Back")
                    }
                    (1..25).forEach { day ->
                        Button(onClick = {
                            state.value = DayState(); selectedDay = getDayInstance(selectedYear!!, day)
                        }) {
                            Text(day.toString())
                        }
                    }
                }
            }
            if (selectedDay != null) {
                val samples = IO.readSamples(selectedDay!!.year, selectedDay!!.day)
                DayLayout(selectedDay!!, samples, state)
            }
        }
    }
}

@Composable
private fun DayLayout(day: Day<Any>, samples: Samples?, state: MutableState<DayState>) =
    Column(modifier = Modifier.fillMaxSize()) {
        val cScope = rememberCoroutineScope()
        Button(onClick = { runDayComposeSingle(day, samples, true, state, cScope) }) {
            Icon(Icons.Default.PlayArrow, "")
            Text("Run")
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
        val time = times[index]
        val result = results[index]
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Sample $part.$index: ")
            TimeValue(time ?: 0.0)
            ResultIcon(result)
            Spacer(Modifier.width(2.dp))
            TextValue(result?.result ?: "")
        }
    }
}

@Composable
private fun InitLayout(time: Double?) = Row(
    modifier = Modifier.border(1.dp, Color.LightGray),
    verticalAlignment = Alignment.CenterVertically
) {
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

private fun runDayComposeSingle(
    day: Day<Any>,
    samples: Samples?,
    runSamples: Boolean,
    state: MutableState<DayState>,
    cScope: CoroutineScope
) {
    cScope.launch {
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
            val init = cScope.async {
                runInit(day, rawInput)
            }
            cScope.launch {
                while (init.isActive) {
                    state.value = state.value.copy(initTime = (System.nanoTime() - initStartTime) / 1000000000.0)
                    delay(100)
                }
            }
            val input = init.await()
            val initTime = (System.nanoTime() - initStartTime) / 1000000000.0

            state.value = state.value.copy(runningInit = false, initTime = initTime, runningPart1 = true)

            val part1StartTime = System.nanoTime()
            val part1 = cScope.async {
                runPart(day, 1, input, expected[Triple(day.year, day.day, 1)])
            }
            cScope.launch {
                while (part1.isActive) {
                    state.value = state.value.copy(part1Time = (System.nanoTime() - part1StartTime) / 1000000000.0)
                    delay(100)
                }
            }
            val part1Result = part1.await()
            val part1Time = (System.nanoTime() - part1StartTime) / 1000000000.0

            state.value = state.value.copy(
                runningPart1 = false,
                part1Time = part1Time,
                part1Result = part1Result,
                runningPart2 = true
            )

            val part2StartTime = System.nanoTime()
            val part2 = cScope.async {
                runPart(day, 2, input, expected[Triple(day.year, day.day, 2)])
            }
            cScope.launch {
                while (part2.isActive) {
                    state.value = state.value.copy(part2Time = (System.nanoTime() - part2StartTime) / 1000000000.0)
                    delay(100)
                }
            }
            val part2Result = part2.await()
            val part2Time = (System.nanoTime() - part2StartTime) / 1000000000.0
            state.value = state.value.copy(runningPart2 = false, part2Time = part2Time, part2Result = part2Result)
        }
    }
}
