package utils

import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

class Point(x: Long, y: Long) : PFNode(x, y) {

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

    fun neighbours() = listOf(left(1), up(1), right(1), down(1))

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

    constructor(raw: String, delim: String = ",") : this(
        raw.split(delim)[0].toLong(),
        raw.split(delim)[1].toLong(),
        raw.split(delim)[2].toLong()
    )

    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    fun distance(p: Point3D) = (max(x, p.x) - min(x, p.x)) + (max(y, p.y) - min(y, p.y)) + (max(z, p.z) - min(z, p.z))

    fun abs(): Long = x.absoluteValue + y.absoluteValue + z.absoluteValue

    operator fun plus(p: Point3D) = Point3D(this.x + p.x, this.y + p.y, this.z + p.z)
}

data class FloatPoint(val x: Float, val y: Float)

data class DoublePoint(val x: Double, val y: Double)

data class DoubleLine(val start: DoublePoint, val end: DoublePoint) {
    val isVertical = start.x == end.x
    val isHorizontal = start.y == end.y

    val slope: Double get() = (end.y - start.y) / (end.x - start.x)

    operator fun get(x: Double): DoublePoint {
        if (isVertical) {
            throw IllegalStateException("Can not get point by x for a vertical line")
        }

        val dx = (x - start.x)
        val y = start.y + dx * slope
        return DoublePoint(x, y)
    }

    operator fun contains(p: DoublePoint): Boolean {
        if (p.y !in (minOf(start.y, end.y) .. maxOf(start.y, end.y))) {
            return false
        }
        if (p.x !in (start.x .. end.x)) {
            return false
        }
        return if (end.x == start.x) {
            true
        } else {
            this[p.x] == p
        }
    }
}

data class Point4D(val x: Long, val y: Long, val z: Long, val w: Long) {
    operator fun minus(o: Point4D) = Point4D(x - o.x, y - o.y, z - o.z, w - o.w)

    operator fun plus(o: Point4D) = Point4D(x + o.x, y + o.y, z + o.z, w + o.w)

    operator fun times(v: Long) = Point4D(x * v, y * v, z * v, w * v)

    val adjacent
        get() = listOf(
            copy(x = x - 1),
            copy(x = x + 1),
            copy(y = y - 1),
            copy(y = y + 1),
            copy(z = z - 1),
            copy(z = z + 1)
        )

    override fun toString(): String {
        return "($x,$y,$z,$w)"
    }

    fun distanceSqr(o: Point4D): Long {
        val d = this - o
        return d.x * d.x + d.y * d.y + d.z * d.z
    }

    fun distanceManhattan(o: Point4D): Long {
        val d = this - o
        return abs(d.x) + abs(d.y) + abs(d.z)
    }

    fun coerceAtLeast(value: Point4D) = Point4D(
        x.coerceAtLeast(value.x),
        y.coerceAtLeast(value.y),
        z.coerceAtLeast(value.z),
        w.coerceAtLeast(value.w)
    )

    fun coerceAtMost(value: Point4D) = Point4D(
        x.coerceAtMost(value.x),
        y.coerceAtMost(value.y),
        z.coerceAtMost(value.z),
        w.coerceAtMost(value.w)
    )

    val asPoint get() = copy(w = 1)
    val asVector get() = copy(w = 0)
}