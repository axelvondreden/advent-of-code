package utils

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Long, val y: Long) {

    constructor(raw: String, delim: String = ",") : this(raw.split(delim)[0].toLong(), raw.split(delim)[1].toLong())

    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    fun distance(step: Point) = (max(x, step.x) - min(x, step.x)) + (max(y, step.y) - min(y, step.y))

    operator fun plus(p: Point) = Point(this.x + p.x, this.y + p.y)
}

data class Point3D(val x: Long, val y: Long, val z: Long) {

    constructor(raw: String, delim: String = ",") : this(raw.split(delim)[0].toLong(), raw.split(delim)[1].toLong(), raw.split(delim)[2].toLong())

    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    fun distance(p: Point3D) = (max(x, p.x) - min(x, p.x)) + (max(y, p.y) - min(y, p.y)) + (max(z, p.z) - min(z, p.z))

    fun abs() : Long = x.absoluteValue + y.absoluteValue + z.absoluteValue

    operator fun plus(p: Point3D) = Point3D(this.x + p.x, this.y + p.y, this.z + p.z)
}