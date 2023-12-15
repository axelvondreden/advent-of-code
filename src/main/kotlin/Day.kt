import kotlinx.coroutines.coroutineScope

abstract class Day<T>(val year: Int, val day: Int) {

    abstract fun List<String>.parse(): T

    abstract fun solve1(input: T): Any

    abstract fun solve2(input: T): Any

    suspend fun visualize1(input: T, onProgress: (String?) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return coroutineScope {
            solve1Visualized(input) { progress ->
                onProgress(progress)
                awaitSignal()
            }
        }
    }

    suspend fun visualize2(input: T, onProgress: (String?) -> Unit, awaitSignal: suspend () -> Unit): Any {
        return coroutineScope {
            solve2Visualized(input) { progress ->
                onProgress(progress)
                awaitSignal()
            }
        }
    }

    open suspend fun solve1Visualized(input: T, onProgress: suspend (String) -> Unit): Any {
        return 0
    }

    open suspend fun solve2Visualized(input: T, onProgress: suspend (String) -> Unit): Any {
        return 0
    }
}