package utils

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

object IO {
    fun readInts(year: Int, day: Int): List<Int> {
        try {
            Files.lines(Paths.get("src/main/resources/$year/day${day.toString().padStart(2, '0')}.txt")).use { return it.map(String::toInt).toList<Int>() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun readStrings(year: Int, day: Int): List<String> {
        try {
            Files.lines(Paths.get("src/main/resources/$year/day${day.toString().padStart(2, '0')}.txt")).use { lines -> return lines.toList() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun readCharMatrix(year: Int, day: Int): Array<CharArray> {
        val list = readStrings(year, day)
        val max = list.map { it.length }.maxOrNull()!!
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
            Files.lines(Paths.get("src/main/resources/$year/day${day.toString().padStart(2, '0')}.txt")).use { lines -> return lines.findFirst().orElse("") }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }

    fun readIntArray(year: Int, day: Int, delim: String = ","): IntArray {
        try {
            Files.lines(Paths.get("src/main/resources/$year/day${day.toString().padStart(2, '0')}.txt")).use {
                return it.findFirst().orElse("").split(delim.toRegex()).map(String::toInt).toIntArray()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return IntArray(0)
        }
    }

    fun readLongArray(year: Int, day: Int, delim: String = ","): LongArray {
        try {
            Files.lines(Paths.get("src/main/resources/$year/day${day.toString().padStart(2, '0')}.txt")).use {
                return it.findFirst().orElse("").split(delim.toRegex()).map(String::toLong).toLongArray()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return LongArray(0)
        }
    }
}