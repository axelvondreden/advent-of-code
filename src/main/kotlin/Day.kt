import utils.IO

abstract class Day(private val year: Int, private val day: Int) {
    abstract fun solve1(): Any
    abstract fun solve2(): Any
    abstract val input: Any

    protected fun readString() = IO.readString(year, day)
    protected fun readStrings() = IO.readStrings(year, day)
    protected fun readInts() = IO.readInts(year, day)
    protected fun readIntArray(delim: String = ",") = IO.readIntArray(year, day, delim)
    protected fun readLongArray(delim: String = ",") = IO.readLongArray(year, day, delim)
    protected fun readCharMatrix() = IO.readCharMatrix(year, day)
}