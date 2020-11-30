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

fun Int.sqrt() = kotlin.math.sqrt(this.toDouble()).roundToInt()

fun Int.gcd(other: Int): Int {
    var n1 = if (this > 0) this else -this
    var n2 = if (other > 0) other else -other
    while (n1 != n2) {
        if (n1 > n2) n1 -= n2 else n2 -= n1
    }
    return n1
}