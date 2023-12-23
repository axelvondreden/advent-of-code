package runner.compose

import Day
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import expected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import runner.ResultState
import runner.VizState

@Composable
fun VizLayout(
    day: Day<Any>,
    state: VizState,
    viz: MutableState<VizGrid?>,
    delay: MutableState<Long>,
    scope: CoroutineScope,
    onDelayChange: (Long) -> Unit,
    onStart: () -> Unit,
    onEnd: () -> Unit
) {
    var jobRunning by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxHeight()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    onStart()
                    jobRunning = true
                    scope.launch(Dispatchers.IO) {
                        runPartVisualized(
                            day = day,
                            part = state.part,
                            input = state.input,
                            onProgress = { viz.value = it },
                            onEnd = {
                                state.result.value = it
                                onEnd()
                                jobRunning = false
                            },
                            vizDelay = { delay.value }
                        )
                    }
                },
                enabled = !jobRunning
            ) {
                Icon(Icons.Default.PlayArrow, "")
                Text("Run")
            }
            Label("Init: ", minWidth = 60.dp)
            TimeValue(state.initTime / 1000000000.0)
            Spacer(Modifier.width(8.dp))
            Label("Delay: ")
            Slider(
                value = delay.value.toFloat(),
                onValueChange = { onDelayChange(it.toLong()) },
                valueRange = 0f..1000f,
                steps = 500,
                modifier = Modifier.width(200.dp)
            )
            Spacer(Modifier.width(6.dp))
            TextValue("${delay.value} ms")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Progress: ", minWidth = 80.dp)
            if (viz.value?.progress != null) {
                TextValue("%.2f".format((viz.value?.progress ?: 0.0) * 100) + " %")
                Spacer(Modifier.width(2.dp))
            } else {
                TextValue("? %")
                Spacer(Modifier.width(2.dp))
            }
            LinearProgressIndicator(progress = viz.value?.progress?.toFloat() ?: 0F, Modifier.fillMaxWidth(0.7F))
            ResultIcon(state.result.value)
            Spacer(Modifier.width(2.dp))
            state.result.value?.result?.let { TextValue(it) }
        }

        val vizVal = viz.value
        if (vizVal != null) {
            VizInfoRow(vizVal)
            vizVal.draw()
        }
    }
}

@Composable
private fun VizInfoRow(viz: VizGrid) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Label("Size:")
        TextValue("${viz.width}x${viz.height}")
        Spacer(Modifier.width(8.dp))
        viz.info.forEach { (key, value) ->
            Label("$key:")
            TextValue(value)
            Spacer(Modifier.width(8.dp))
        }
    }
}

private suspend fun runPartVisualized(
    day: Day<Any>,
    part: Int,
    input: Any,
    onProgress: (VizGrid) -> Unit,
    onEnd: (ResultState) -> Unit,
    vizDelay: () -> Long
) {
    val result = if (part == 1) {
        runPart1WithVisualization(day, input, expected[Triple(day.year, day.day, 1)], onProgress, vizDelay)
    } else {
        runPart2WithVisualization(day, input, expected[Triple(day.year, day.day, 2)], onProgress, vizDelay)
    }
    onEnd(result)
}

suspend fun runPart1WithVisualization(
    day: Day<Any>,
    input: Any,
    expected: String?,
    onProgress: (VizGrid) -> Unit,
    delay: () -> Long
): ResultState {
    val result = day.visualize1(input, onProgress = onProgress, awaitSignal = { delay(delay()) }).toString()
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    return ResultState(result, isCorrect)
}

suspend fun runPart2WithVisualization(
    day: Day<Any>,
    input: Any,
    expected: String?,
    onProgress: (VizGrid) -> Unit,
    delay: () -> Long
): ResultState {
    val result = day.visualize2(input, onProgress = onProgress, awaitSignal = { delay(delay()) }).toString()
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    return ResultState(result, isCorrect)
}