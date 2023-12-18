package runner.compose

import Day
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    viz: MutableState<Viz?>,
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
            VizGrid(vizVal)
        }
    }
}

@Composable
private fun VizGrid(viz: Viz) {
    Row(Modifier.fillMaxSize().padding(6.dp)) {
        for (x in 0 until viz.width) {
            Column(modifier = Modifier.fillMaxHeight().weight(1F)) {
                for (y in 0 until viz.height) {
                    val tile = viz.map[x][y]
                    Tile(tile)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Tile(tile: Tile) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .border(1.dp, tile.borderColor, shape = tile.borderShape())
            .background(tile.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        val w = minWidth
        Text(
            tile.char?.toString() ?: "",
            color = tile.color,
            fontSize = w.value.sp / 1.3,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

private suspend fun runPartVisualized(
    day: Day<Any>,
    part: Int,
    input: Any,
    onProgress: (Viz) -> Unit,
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
    onProgress: (Viz) -> Unit,
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
    onProgress: (Viz) -> Unit,
    delay: () -> Long
): ResultState {
    val result = day.visualize2(input, onProgress = onProgress, awaitSignal = { delay(delay()) }).toString()
    val isCorrect = !expected.isNullOrEmpty() && expected == result
    return ResultState(result, isCorrect)
}