package utils

import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int) {

    constructor(raw: String, delim: String = ",") : this(raw.split(delim)[0].toInt(), raw.split(delim)[1].toInt())

    fun distance(step: Point): Int = (max(x, step.x) - min(x, step.x)) + (max(y, step.y) - min(y, step.y))

    operator fun plus(p: Point) = Point(this.x + p.x, this.y + p.y)
}