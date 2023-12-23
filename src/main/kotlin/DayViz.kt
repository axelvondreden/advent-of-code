import kotlinx.coroutines.coroutineScope
import runner.compose.Viz
import runner.compose.VizGrid

abstract class DayViz<INPUT, VIZ : Viz>(year: Int, day: Int) : Day<INPUT>(year, day) {

    suspend fun visualize1(input: INPUT, onProgress: (VIZ) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return coroutineScope {
            solve1Visualized(input) { viz ->
                onProgress(viz)
                awaitSignal()
            }
        }
    }

    suspend fun visualize2(input: INPUT, onProgress: (VIZ) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return coroutineScope {
            solve2Visualized(input) { viz ->
                onProgress(viz)
                awaitSignal()
            }
        }
    }

    open fun initViz1(input: INPUT): Viz = VizGrid()
    open fun initViz2(input: INPUT): Viz = VizGrid()

    open suspend fun solve1Visualized(input: INPUT, onProgress: suspend (VIZ) -> Unit): Any {
        return 0
    }

    open suspend fun solve2Visualized(input: INPUT, onProgress: suspend (VIZ) -> Unit): Any {
        return 0
    }

    open val vizWidth = 30
    open val vizHeight = 10
    open val vizDelay = 20L
}