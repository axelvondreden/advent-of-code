package runner

import androidx.compose.runtime.MutableState

data class DayState(
    var initStartTime: MutableState<Long>,
    var initTime: MutableState<Long>,
    var part1StartTime: MutableState<Long>,
    var part1Time: MutableState<Long>,
    var part2StartTime: MutableState<Long>,
    var part2Time: MutableState<Long>,
    var part1Result: MutableState<ResultState?>,
    var part2Result: MutableState<ResultState?>,
    //val part1SampleTimes: MutableMap<Int, Double> = mutableMapOf(),
    //val part2SampleTimes: MutableMap<Int, Double> = mutableMapOf(),
    //val part1SampleResults: MutableMap<Int, ResultState> = mutableMapOf(),
    //val part2SampleResults: MutableMap<Int, ResultState> = mutableMapOf()
) {
    fun reset() {
        initStartTime.value = 0L
        initTime.value = 0L
        part1StartTime.value = 0L
        part1Time.value = 0L
        part2StartTime.value = 0L
        part2Time.value = 0L
        part1Result.value = null
        part2Result.value = null
    }
}


data class ResultState(val result: String, val correct: Boolean)