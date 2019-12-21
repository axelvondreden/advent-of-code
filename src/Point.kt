import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int) {

    constructor(raw: String, delim: String = ",") : this(raw.split(delim)[0].toInt(), raw.split(delim)[1].toInt())

    fun distance(step: Point): Int = (max(x, step.x) - min(x, step.x)) + (max(y, step.y) - min(y, step.y))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}