import kotlinx.coroutines.coroutineScope

abstract class Day<T>(val year: Int, val day: Int) {

    abstract fun List<String>.parse(): T

    abstract fun solve1(input: T): Any

    abstract fun solve2(input: T): Any

    open suspend fun visualize1(input: T, onProgress: (Double?) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return 0
    }

    suspend fun visualize1(input: String, onProgress: (Double?) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return coroutineScope {
            doComputation(input) { progress ->
                onProgress(progress)
                awaitSignal()
            }
        }
    }

    open suspend fun doComputation(input: String, onProgress: suspend (Double) -> Unit): Any {
        return 0
    }
}