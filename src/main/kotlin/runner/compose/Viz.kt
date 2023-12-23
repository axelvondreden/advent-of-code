package runner.compose

import androidx.compose.runtime.Composable

abstract class Viz(val progress: Double?) {
    val info = mutableMapOf<String, String>()

    @Composable
    abstract fun drawViz()
}