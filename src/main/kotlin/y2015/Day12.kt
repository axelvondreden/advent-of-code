package y2015

import Day
import com.google.gson.JsonElement
import com.google.gson.JsonParser

class Day12 : Day(2015, 12) {

    override val input = readString()

    private val integerPattern = Regex("-?\\d+")

    override fun solve1() = integerPattern.findAll(input).mapNotNull { it.value.toInt() }.sum()

    override fun solve2() = jsonSum(JsonParser.parseString(input))

    private fun jsonSum(node: JsonElement): Int {
        return when {
            node.isJsonPrimitive -> try { node.asInt } catch (e: Exception) { 0 }
            node.isJsonArray -> node.asJsonArray.map { jsonSum(it) }.sum()
            node.isJsonObject -> {
                node.asJsonObject.takeIf { subNode ->
                    subNode.entrySet().none { it.value.isRed() }
                }?.entrySet()?.map {
                    jsonSum(it.value)
                }?.sum() ?: 0
            }
            else -> throw Exception("bad node type")
        }
    }

    private fun JsonElement.isRed() = this.isJsonPrimitive && this.asString == "red"
}