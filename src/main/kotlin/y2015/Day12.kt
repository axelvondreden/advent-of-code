package y2015

import Day

//import jdk.nashorn.api.scripting.ScriptObjectMirror
//import jdk.nashorn.internal.runtime.JSONListAdapter
//import javax.script.ScriptEngineManager

class Day12 : Day(2015, 12) {

    override val input = 0
        //ScriptEngineManager().getEngineByName("javascript").eval(
        //    "Java.asJSONCompatible(${utils.Utils.readString(2015, 12)})"
        //) as Map<*, *>

    override fun solve1() = sum(input)

    override fun solve2() = sumNotRed(input)

    private fun sum(obj: Any): Int {
        return when (obj) {
            is Int -> obj
            //is JSONListAdapter -> obj.sumBy { sum(it) }
            //is ScriptObjectMirror -> obj.values.sumBy { sum(it) }
            else -> 0
        }
    }

    private fun sumNotRed(obj: Any): Int {
        return when (obj) {
            is Int -> obj
            //is JSONListAdapter -> obj.sumBy { sumNotRed(it) }
            //is ScriptObjectMirror -> if (obj.values.any { it == "red" }) 0 else obj.values.sumBy { sumNotRed(it) }
            else -> 0
        }
    }
}