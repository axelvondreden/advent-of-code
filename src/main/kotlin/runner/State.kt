package runner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap

data class DayState(
    val initStartTime: MutableState<Long>,
    val initTime: MutableState<Long>,
    val part1StartTime: MutableState<Long>,
    val part1Time: MutableState<Long>,
    val part2StartTime: MutableState<Long>,
    val part2Time: MutableState<Long>,
    val part1Result: MutableState<ResultState?>,
    val part2Result: MutableState<ResultState?>,
    val part1SampleTimes: SnapshotStateMap<Int, Pair<Long, Long>>,
    val part2SampleTimes: SnapshotStateMap<Int, Pair<Long, Long>>,
    val part1SampleResults: SnapshotStateMap<Int, ResultState>,
    val part2SampleResults: SnapshotStateMap<Int, ResultState>,
    val target: MutableState<Target?>
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
        part1SampleTimes.clear()
        part2SampleTimes.clear()
        part1SampleResults.clear()
        part2SampleResults.clear()
        target.value = null
    }
}

data class ResultState(val result: String, val correct: Boolean)

sealed class Target(var nr: Int) {
    data object INIT : Target(0)
    data object PART1 : Target(0)
    data object PART2 : Target(0)
    class SAMPLE1(nr: Int) : Target(nr)
    class SAMPLE2(nr: Int) : Target(nr)
}