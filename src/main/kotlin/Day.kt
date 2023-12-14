import kotlinx.coroutines.coroutineScope

abstract class Day<T>(val year: Int, val day: Int) {

    abstract fun List<String>.parse(): T

    abstract fun solve1(input: T): Any

    abstract fun solve2(input: T): Any

    suspend fun visualize1(input: T, onProgress: (String?) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return coroutineScope {
            doComputation(input) { progress ->
                onProgress(progress)
                awaitSignal()
            }
        }
    }

    open suspend fun doComputation(input: T, onProgress: suspend (String) -> Unit): Any {
        return 0
    }
}