package utils

import kotlin.math.pow
import kotlin.math.roundToInt

fun Int.getAllDivisors(divisors: MutableSet<Int>) {
    val root = sqrt()
    for (i in 1..(root + 1)) {
        if (this % i == 0) {
            divisors.add(i)
            if (i * i != this) {
                divisors.add(this / i)
            }
        }
    }
}

fun Long.pow(exp: Int) = toDouble().pow(exp).toLong()

fun Int.sqrt() = kotlin.math.sqrt(this.toDouble()).roundToInt()

fun Int.gcd(other: Int): Int {
    var n1 = if (this > 0) this else -this
    var n2 = if (other > 0) other else -other
    while (n1 != n2) {
        if (n1 > n2) n1 -= n2 else n2 -= n1
    }
    return n1
}

fun Long.gcd(other: Long): Long {
    var n1 = if (this > 0) this else -this
    var n2 = if (other > 0) other else -other
    while (n1 != n2) {
        if (n1 > n2) n1 -= n2 else n2 -= n1
    }
    return n1
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun Int.stepSum(): Int {
    var num = this
    var fac = num
    while (num > 0) {
        fac += --num
    }
    return fac
}