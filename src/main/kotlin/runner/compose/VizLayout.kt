package runner.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import runner.Target
import runner.Viz
import runner.VizState

@Composable
fun VizLayout(state: VizState, delay: Long, onDelayChange: (Long) -> Unit) {
    Column(Modifier.fillMaxHeight()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LaunchedEffect(state.target.value) {
                while (state.target.value == Target.Init) {
                    delay(100)
                    state.initTime.value = System.nanoTime() - state.initStartTime.value
                }
            }
            Label("Init: ", minWidth = 60.dp)
            TimeValue(state.initTime.value / 1000000000.0)
            Spacer(Modifier.width(8.dp))
            Label("Delay: ")
            Slider(
                value = delay.toFloat(),
                onValueChange = { onDelayChange(it.toLong()) },
                valueRange = 0f..1000f,
                steps = 500,
                modifier = Modifier.width(200.dp)
            )
            Spacer(Modifier.width(6.dp))
            TextValue("$delay ms")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Label("Progress: ", minWidth = 80.dp)
            if (state.viz.value.progress != null) {
                TextValue("%.2f".format((state.viz.value.progress ?: 0.0) * 100) + " %")
                Spacer(Modifier.width(2.dp))
            } else {
                TextValue("? %")
                Spacer(Modifier.width(2.dp))
            }
            LinearProgressIndicator(progress = state.viz.value.progress?.toFloat() ?: 0F, Modifier.fillMaxWidth(0.7F))
            ResultIcon(state.result.value)
            Spacer(Modifier.width(2.dp))
            state.result.value?.result?.let { TextValue(it) }
        }

        VizGrid(state.viz.value)
    }
}

@Composable
private fun VizGrid(viz: Viz) {
    Row(Modifier.fillMaxSize()) {
        for (x in 0 until viz.width) {
            Column(modifier = Modifier.fillMaxHeight().weight(1F)) {
                for (y in 0 until viz.height) {
                    val tile = viz.map[x][y]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                            .border(1.dp, tile.borderColor)
                            .background(tile.backgroundColor)
                    ) {
                        Text(tile.char?.toString() ?: "", color = tile.color)
                    }
                }
            }
        }
    }
}