package utils

import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class Point(x: Long, y: Long): PFNode(x, y) {

    constructor(raw: String, delim: String = ",") : this(raw.split(delim)[0].toLong(), raw.split(delim)[1].toLong())

    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    fun distance(step: Point) = (max(x, step.x) - min(x, step.x)) + (max(y, step.y) - min(y, step.y))

    operator fun plus(p: Point) = Point(this.x + p.x, this.y + p.y)

    operator fun plus(pair: Pair<Int, Int>) = Point(x + pair.first, y + pair.second)

    operator fun plus(dir: Dir) = when (dir) {
        Dir.UP -> up(1)
        Dir.DOWN -> down(1)
        Dir.LEFT -> left(1)
        Dir.RIGHT -> right(1)
    }

    operator fun minus(pair: Pair<Int, Int>) = Point(x - pair.first, y - pair.second)

    operator fun times(i: Int) = Point(this.x * i, this.y * i)

    infix fun left(steps: Int) = Point(x - steps, y)
    infix fun right(steps: Int) = Point(x + steps, y)
    infix fun up(steps: Int) = Point(x, y - steps)
    infix fun down(steps: Int) = Point(x, y + steps)

    fun copy(x: Long = this.x, y: Long = this.y) = Point(x, y)

    override fun toString(): String {
        return "Point($x, $y)"
    }
}

data class Line(val p1: Point, val p2: Point) {
    fun isStraight() = p1.x == p2.x || p1.y == p2.y
    fun getPoints(): List<Point> {
        var p = p1
        val list = mutableListOf(p)
        while (p != p2) {
            val dx = (p2.x - p.x).let { if (it == 0L) 0L else it / abs(it) }
            val dy = (p2.y - p.y).let { if (it == 0L) 0L else it / abs(it) }
            p += Point(dx, dy)
            list += p
        }
        return list
    }
}

data class Point3D(val x: Long, val y: Long, val z: Long) {

    constructor(raw: String, delim: String = ",") : this(raw.split(delim)[0].toLong(), raw.split(delim)[1].toLong(), raw.split(delim)[2].toLong())

    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    fun distance(p: Point3D) = (max(x, p.x) - min(x, p.x)) + (max(y, p.y) - min(y, p.y)) + (max(z, p.z) - min(z, p.z))

    fun abs() : Long = x.absoluteValue + y.absoluteValue + z.absoluteValue

    operator fun plus(p: Point3D) = Point3D(this.x + p.x, this.y + p.y, this.z + p.z)
}