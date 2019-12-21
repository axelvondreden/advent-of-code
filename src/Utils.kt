import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

object Utils {
    fun readInts(year: Int, day: Int): List<Int> {
        try {
            Files.lines(Paths.get("res/$year/day${day.toString().padStart(2, '0')}.txt")).use { return it.map(String::toInt).toList<Int>() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun readStrings(year: Int, day: Int): List<String> {
        try {
            Files.lines(Paths.get("res/$year/day${day.toString().padStart(2, '0')}.txt")).use { lines -> return lines.toList() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun readCharMatrix(year: Int, day: Int): Array<CharArray> {
        val list = readStrings(year, day)
        val max = list.map { it.length }.max()!!
        val matrix = Array(max) { CharArray(list.size) }
        for (y in list.indices) {
            val s = list[y]
            for (x in s.indices) {
                matrix[x][y] = s[x]
            }
        }
        return matrix
    }

    fun readString(year: Int, day: Int): String {
        try {
            Files.lines(Paths.get("res/$year/day${day.toString().padStart(2, '0')}.txt")).use { lines -> return lines.findFirst().orElse("") }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }

    fun readIntArray(year: Int, day: Int, delim: String = ","): IntArray {
        try {
            Files.lines(Paths.get("res/$year/day${day.toString().padStart(2, '0')}.txt")).use {
                return it.findFirst().orElse("").split(delim.toRegex()).map(String::toInt).toIntArray()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return IntArray(0)
        }
    }

    fun readLongArray(year: Int, day: Int, delim: String = ","): LongArray {
        try {
            Files.lines(Paths.get("res/$year/day${day.toString().padStart(2, '0')}.txt")).use {
                return it.findFirst().orElse("").split(delim.toRegex()).map(String::toLong).toLongArray()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return LongArray(0)
        }
    }

    fun <T> permute(list: List<T>): List<List<T>> {
        if (list.size == 1) return listOf(list)
        val perms = mutableListOf<List<T>>()
        val sub = list[0]
        for (perm in permute(list.drop(1)))
            for (i in 0..perm.size) {
                val newPerm = perm.toMutableList()
                newPerm.add(i, sub)
                perms.add(newPerm)
            }
        return perms
    }
}