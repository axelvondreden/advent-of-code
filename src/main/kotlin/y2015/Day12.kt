package y2015

import Day
import com.google.gson.JsonElement
import com.google.gson.JsonParser

class Day12 : Day(2015, 12) {

    override val input = readString()

    private val integerPattern = Regex("-?\\d+")

    override fun solve1() = integerPattern.findAll(input).map { it.value.toInt() }.sum()

    override fun solve2() = JsonParser.parseString(input).sum()

    private fun JsonElement.sum(): Int {
        return when {
            isJsonPrimitive -> try { asInt } catch (e: Exception) { 0 }
            isJsonArray -> asJsonArray.sumOf { it.sum() }
            isJsonObject -> asJsonObject.takeIf { subNode -> subNode.entrySet().none { it.value.isRed() } }?.entrySet()?.sumOf { it.value.sum() } ?: 0
            else -> throw Exception("bad node type")
        }
    }

    private fun JsonElement.isRed() = this.isJsonPrimitive && this.asString == "red"
}