package utils

import Samples
import com.google.gson.Gson
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

object IO {

    fun readStrings(year: Int, day: Int): List<String> {
        try {
            Files.lines(Paths.get("src/main/resources/$year/day${day.toString().padStart(2, '0')}.txt")).use { lines -> return lines.toList() }
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun readSamplesRaw(year: Int, day: Int): String {
        try {
            Files.lines(Paths.get("src/main/resources/$year/day${day.toString().padStart(2, '0')}.json")).use { lines -> return lines.toList().joinToString("") }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }

    fun readSamples(year: Int, day: Int): Samples? {
        val text = readSamplesRaw(year, day)
        return Gson().fromJson(text, Samples::class.java)
    }
}