package runner

data class DayState(
    var runningInit: Boolean = false,
    var runningPart1: Boolean = false,
    var runningPart2: Boolean = false,
    var runningPart1Sample: Int? = null,
    var runningPart2Sample: Int? = null,
    var initTime: Double? = null,
    var part1Time: Double? = null,
    var part2Time: Double? = null,
    val part1SampleTimes: MutableMap<Int, Double> = mutableMapOf(),
    val part2SampleTimes: MutableMap<Int, Double> = mutableMapOf(),
    var part1Result: ResultState? = null,
    var part2Result: ResultState? = null,
    val part1SampleResults: MutableMap<Int, ResultState> = mutableMapOf(),
    val part2SampleResults: MutableMap<Int, ResultState> = mutableMapOf()
)


data class ResultState(val result: String, val correct: Boolean)