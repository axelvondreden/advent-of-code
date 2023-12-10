package utils

import Samples
import com.google.gson.Gson
import java.io.IOException

object IO {

    fun readStrings(year: Int, day: Int): List<String> {
        return try {
            IO::class.java.classLoader.getResource("$year/day${day.toString().padStart(2, '0')}.txt")!!.readText().split("\r\n", "\n")
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun readSamplesRaw(year: Int, day: Int) = try {
        IO::class.java.classLoader.getResource("$year/day${day.toString().padStart(2, '0')}.json")!!.readText()
    } catch (e: IOException) {
        ""
    }

    fun readSamples(year: Int, day: Int): Samples? {
        val text = readSamplesRaw(year, day)
        return Gson().fromJson(text, Samples::class.java)
    }
}