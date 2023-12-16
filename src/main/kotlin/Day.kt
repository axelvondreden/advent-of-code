import kotlinx.coroutines.coroutineScope
import runner.compose.Viz

abstract class Day<T>(val year: Int, val day: Int) {

    abstract fun List<String>.parse(): T

    abstract fun solve1(input: T): Any

    abstract fun solve2(input: T): Any

    suspend fun visualize1(input: T, onProgress: (Viz) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return coroutineScope {
            solve1Visualized(input) { viz ->
                onProgress(viz)
                awaitSignal()
            }
        }
    }

    suspend fun visualize2(input: T, onProgress: (Viz) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return coroutineScope {
            solve2Visualized(input) { viz ->
                onProgress(viz)
                awaitSignal()
            }
        }
    }

    open fun initViz1(input: T): Viz = Viz()
    open fun initViz2(input: T): Viz = Viz()

    open suspend fun solve1Visualized(input: T, onProgress: suspend (Viz) -> Unit): Any {
        return 0
    }

    open suspend fun solve2Visualized(input: T, onProgress: suspend (Viz) -> Unit): Any {
        return 0
    }

    open val vizWidth = 30
    open val vizHeight = 10
    open val vizDelay = 20L
}