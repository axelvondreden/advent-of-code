abstract class Day<T>(val year: Int, val day: Int) {

    abstract suspend fun List<String>.parse(): T

    abstract suspend fun solve1(input: T): Any

    abstract suspend fun solve2(input: T): Any
}