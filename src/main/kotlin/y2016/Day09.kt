package y2016

import Day

class Day09 : Day() {

    override val input = Utils.readString(2016, 9)

    override fun solve1() = input.decodeCount()

    override fun solve2() = input.decodeCountRec()

    private fun String.decodeCount(): Long {
        var count = 0L
        var index = 0
        while (index < length) {
            if (get(index) == '(') {
                val endIndex = indexOf(')', index)
                val marker = parseMarker(substring(index + 1, endIndex))
                val s = substring(endIndex + 1, endIndex + marker.chars + 1)
                count += s.length * marker.repeats
                index += ((endIndex - index) + marker.chars + 1)
            } else {
                count++
                index++
            }
        }
        return count
    }

    private fun String.decodeCountRec(): Long {
        var count = 0L
        var index = 0
        while (index < length) {
            if (get(index) == '(') {
                val endIndex = indexOf(')', index)
                val marker = parseMarker(substring(index + 1, endIndex))
                val s = substring(endIndex + 1, endIndex + marker.chars + 1)
                count += s.decodeCountRec() * marker.repeats
                index += ((endIndex - index) + marker.chars + 1)
            } else {
                count++
                index++
            }
        }
        return count
    }

    private fun String.decode(): String {
        var newString = ""
        var index = 0
        while (index < length) {
            if (get(index) == '(') {
                val endIndex = indexOf(')', index)
                val marker = parseMarker(substring(index + 1, endIndex))
                repeat(marker.repeats) {
                    newString += substring(endIndex + 1, endIndex + marker.chars + 1)
                    index += (endIndex - index) + marker.chars + 1
                }
            } else {
                newString += get(index)
                index++
            }
        }
        return newString
    }

    private fun parseMarker(substring: String) = Marker(substring.split("x")[0].toInt(), substring.split("x")[1].toInt())

    data class Marker(val chars: Int, val repeats: Int)
}