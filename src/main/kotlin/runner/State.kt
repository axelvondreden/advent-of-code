package runner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap

data class YearState(
    val initTimes: SnapshotStateMap<Int, Pair<Long, Long>>,
    val part1Times: SnapshotStateMap<Int, Pair<Long, Long>>,
    val part2Times: SnapshotStateMap<Int, Pair<Long, Long>>,
    val part1Results: SnapshotStateMap<Int, ResultState>,
    val part2Results: SnapshotStateMap<Int, ResultState>,
    val target: MutableState<YearTarget?>
) {
    fun reset() {
        initTimes.clear()
        part1Times.clear()
        part2Times.clear()
        part1Results.clear()
        part2Results.clear()
        target.value = null
    }
}

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
    data object Init : Target(0)
    class Part(nr: Int) : Target(nr)
    class Sample1(nr: Int) : Target(nr)
    class Sample2(nr: Int) : Target(nr)
}

sealed class YearTarget(val day: Int) {
    class Init(day: Int) : YearTarget(day)
    class Part1(day: Int) : YearTarget(day)
    class Part2(day: Int) : YearTarget(day)
}

data class VizState(
    val initStartTime: MutableState<Long>,
    val initTime: MutableState<Long>,
    val startTime: MutableState<Long>,
    val time: MutableState<Long>,
    val result: MutableState<ResultState?>,
    val viz: MutableState<Viz>,
    val target: MutableState<Target?>
) {
    fun reset() {
        initStartTime.value = 0L
        initTime.value = 0L
        startTime.value = 0L
        time.value = 0L
        result.value = null
        viz.value = Viz()
        target.value = null
    }
}