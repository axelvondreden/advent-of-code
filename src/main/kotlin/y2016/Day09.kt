package y2016

import Day

class Day09 : Day() {

    override val input = Utils.readString(2016, 9)

    override fun solve1() = input.decode().length

    override fun solve2() = 0

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