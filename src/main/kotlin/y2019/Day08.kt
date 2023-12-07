package y2019

import Day

class Day08 : Day<List<List<Int>>>(2019, 8) {

    override fun List<String>.parse() = first().map { it.toString().toInt() }.chunked(25 * 6)

    override fun solve1(input: List<List<Int>>): Int {
        val minLayer = input.minByOrNull { list -> list.count { it == 0 } }!!
        return minLayer.count { it == 1 } * minLayer.count { it == 2 }
    }

    override fun solve2(input: List<List<Int>>): String {
        val finalImage = IntArray(25 * 6) { 2 }
        finalImage.indices.forEach { i ->
            for (layer in input) {
                if (layer[i] < 2) {
                    finalImage[i] = layer[i]
                    break
                }
            }
        }
        val result = "\n${finalImage.asList().chunked(25).joinToString("") { list -> list.joinToString("") { if (it == 0) " " else "#" } + "\n" }}"
        return if (result == control) "CYKBY" else ""
    }

    private companion object {
        private const val control = """
 ##  #   ##  # ###  #   #
#  # #   ## #  #  # #   #
#     # # ##   ###   # # 
#      #  # #  #  #   #  
#  #   #  # #  #  #   #  
 ##    #  #  # ###    #  
"""
    }
}