package y2023

import Day
import utils.*

class Day24 : Day<List<Day24.Ball>>(2023, 24) {

    override suspend fun List<String>.parse() = map { line ->
        val (pos, vel) = line.cut(" @ ").map { split ->
            val (x, y, z) = split.split(", ").map { it.toLong() }
            Point4D(x, y, z, 0)
        }
        Ball(pos, vel.copy(w = 1))
    }

    override suspend fun solve1(input: List<Ball>): Int {
        val (start, end) = DoublePoint(200000000000000.0, 200000000000000.0) to DoublePoint(400000000000000.0, 400000000000000.0)
        return input.pairs.count { (a, b) -> intersect1(a, b, start, end) }
    }

    override suspend fun solve2(input: List<Ball>): Long {
        System.loadLibrary("libz3")
        return z3 {
            val xT = int("x_t")
            val yT = int("y_t")
            val zT = int("z_t")
            val xvelT = int("xvel_t")
            val yvelT = int("yvel_t")
            val zvelT = int("zvel_t")
            val dt1 = int("dt1")
            val dt2 = int("dt2")
            val dt3 = int("dt3")

            val dt = listOf(dt1, dt2, dt3)

            val eqs = input.take(3).flatMapIndexed { idx, ball ->
                listOf(
                    (xT - ball.position.x) eq (dt[idx] * (ball.velocity.x - xvelT)),
                    (yT - ball.position.y) eq (dt[idx] * (ball.velocity.y - yvelT)),
                    (zT - ball.position.z) eq (dt[idx] * (ball.velocity.z - zvelT)),
                )
            }

            solve(eqs)

            eval(xT + yT + zT).toLong()
        }
    }

    data class Ball(val position: Point4D, val velocity: Point4D)

    private fun intersect1(a: Ball, b: Ball, start: DoublePoint, end: DoublePoint): Boolean {
        val al = DoubleLine(
            DoublePoint(a.position.x.toDouble(), a.position.y.toDouble()),
            DoublePoint((a.position + a.velocity).x.toDouble(), (a.position + a.velocity).y.toDouble())
        )
        val bl = DoubleLine(
            DoublePoint(b.position.x.toDouble(), b.position.y.toDouble()),
            DoublePoint((b.position + b.velocity).x.toDouble(), (b.position + b.velocity).y.toDouble())
        )

        val x = (al[0.0].y - bl[0.0].y) / (bl.slope - al.slope)
        val y = al[x].y

        if (x !in start.x..end.x || y !in start.y..end.y) {
            return false
        }

        val at = (x - a.position.x) / a.velocity.x
        val bt = (x - b.position.x) / b.velocity.x
        return (at > 0.0 && bt > 0.0)
    }

    private val <T> List<T>.pairs: Sequence<Pair<T, T>> get() = selections(2).map { (a, b) -> a to b }

    private fun <T> List<T>.selections(count: Int): Sequence<List<T>> {
        val list = MutableList(size) { this[it] }
        val take = BooleanArray(size) { it >= size - count }

        return generateSequence(subList(size - count, size)) {
            val last = take.lastIndexOf(true)
            val swap = take.asList().subList(0, last).lastIndexOf(false)
            if (swap == -1) {
                return@generateSequence null
            }

            val shiftCount = last - swap - 1
            (size - shiftCount until size).forEach {
                take[it] = true
            }
            (swap + 2 until size - shiftCount).forEach {
                take[it] = false
            }

            take[swap] = true
            take[swap + 1] = false

            list.clear()
            take.forEachIndexed { i, b ->
                if (b) {
                    list.add(this[i])
                }
            }
            return@generateSequence list
        }
    }

    private fun String.cut(delimiter: String = ","): Pair<String, String> {
        val idx = indexOf(delimiter)
        require(idx != -1)
        return substring(0, idx).trim() to substring(idx + delimiter.length).trim()
    }

    private fun <A, B> Pair<A, A>.map(fn: (A) -> B): Pair<B, B> = fn(first) to fn(second)
}