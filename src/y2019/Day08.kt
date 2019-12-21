package y2019

import Day
import Utils

class Day08 : Day() {

    override val input = Utils.readString(2019, 8).map { it.toString().toInt() }.chunked(25 * 6)

    override fun solve1(): Int {
        val minLayer = input.minBy { it.count { it == 0 } }!!
        return minLayer.count { it == 1 } * minLayer.count { it == 2 }
    }

    override fun solve2(): String {
        val finalImage = IntArray(25 * 6) { 2 }
        for (i in finalImage.indices) {
            for (layer in input) {
                if (layer[i] < 2) {
                    finalImage[i] = layer[i]
                    break
                }
            }
        }
        return "\n\t\t${finalImage.asList().chunked(25).joinToString("") { it.joinToString("") { if (it == 0) " " else "#" } + "\n\t\t" }}"
    }
}